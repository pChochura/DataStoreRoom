package com.pointlessapps.datastoreroom.processor.generators

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.pointlessapps.datastoreroom.annotations.TypeConverter
import com.pointlessapps.datastoreroom.converters.DataStoreTypeConverter
import com.pointlessapps.datastoreroom.processor.*
import com.pointlessapps.datastoreroom.processor.getDataStorePropertyName
import com.pointlessapps.datastoreroom.processor.isTypeOf
import com.pointlessapps.datastoreroom.processor.startWithUppercase
import com.pointlessapps.datastoreroom.processor.toSnakeCase
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class DataStorePropertyGenerator(private val file: FileSpec.Builder) {

    @Suppress("UNCHECKED_CAST")
    fun generateTypeConverters(properties: Sequence<KSPropertyDeclaration>): List<PropertySpec> {
        val converters = properties.mapNotNull { property ->
            property.annotations.find {
                it.isTypeOf<TypeConverter>()
            }?.let {
                property to it
            }
        }.mapNotNull { (property, annotation) ->
            (annotation.arguments.find {
                it.name?.asString() == "converter"
            }?.value as? KSType)?.let {
                property to it
            }
        }

        return converters.map { (property, converter) ->
            val type = if (converter.declaration.typeParameters.isEmpty()) {
                converter.toClassName()
            } else {
                converter.toClassName().parameterizedBy(property.type.toTypeName())
            }

            PropertySpec.builder("${property.simpleName.getShortName()}Converter", type)
                .initializer("%T()", type)
                .build()
        }.toList()
    }

    fun generateGetLastOrNullMethods(properties: Sequence<KSPropertyDeclaration>): List<FunSpec> {
        file.addImport("kotlinx.coroutines.flow", "lastOrNull")

        return properties.map { property ->
            val shortName = getDataStorePropertyName(property)
            FunSpec.builder("getLastOrNull${shortName.startWithUppercase()}")
                .addModifiers(KModifier.SUSPEND)
                .returns(property.type.toTypeName().copy(nullable = true))
                .addCode("return get${shortName.startWithUppercase()}().lastOrNull()")
                .build()
        }.toList()
    }

    fun generateGetMethods(properties: Sequence<KSPropertyDeclaration>): List<FunSpec> {
        file.addImport("kotlinx.coroutines.flow", "map")

        return properties.map { property ->
            val shortName = getDataStorePropertyName(property)
            val shouldBeConverted = shouldDataStorePropertyBeConverted(property)
            val getter = if (shouldBeConverted) {
                generateConverterGetter(property)
            } else {
                generateGetter(property)
            }
            FunSpec.builder("get${shortName.startWithUppercase()}")
                .returns(
                    FLOW_TYPE.parameterizedBy(
                        property.type.toTypeName().copy(nullable = true),
                    ),
                )
                .addCode(
                    buildCodeBlock {
                        addStatement(
                            """
                                |return context.dataStore.data.map {
                                |   $getter
                                |}
                            """.trimMargin(),
                        )
                    },
                )
                .build()
        }.toList()
    }

    fun generateUpdateMethods(properties: Sequence<KSPropertyDeclaration>): List<FunSpec> {
        file.addImport("androidx.datastore.preferences.core", "edit")

        return properties.map { property ->
            val shortName = getDataStorePropertyName(property)
            val shouldBeConverted = shouldDataStorePropertyBeConverted(property)
            val setter = if (shouldBeConverted) {
                generateConverterSetter(property)
            } else {
                generateSetter(property)
            }
            FunSpec.builder("update${shortName.startWithUppercase()}")
                .addModifiers(KModifier.SUSPEND)
                .addParameter(shortName, property.type.toTypeName())
                .addCode(
                    buildCodeBlock {
                        addStatement(
                            """
                                |return context.dataStore.edit {
                                |   $setter
                                |}
                            """.trimMargin(),
                        )
                    },
                )
                .build()
        }.toList()
    }

    fun generateRemoveMethods(properties: Sequence<KSPropertyDeclaration>): List<FunSpec> {
        file.addImport("androidx.datastore.preferences.core", "edit")

        return properties.map { property ->
            val shortName = getDataStorePropertyName(property)
            FunSpec.builder("remove${shortName.startWithUppercase()}")
                .addModifiers(KModifier.SUSPEND)
                .addCode("context.dataStore.edit { it.remove(KEY_${shortName.toSnakeCase()}) }")
                .build()
        }.toList()
    }

    fun generateCompanionObject(properties: Sequence<KSPropertyDeclaration>): TypeSpec {
        val companionObject = TypeSpec.companionObjectBuilder()
            .addModifiers(KModifier.PRIVATE)

        properties.forEach { property ->
            val propertyTypeName = getPropertyTypeName(property)
            val initializerName = getPropertyInitializer(propertyTypeName)

            file.addImport("androidx.datastore.preferences.core", initializerName)

            val shortName = getDataStorePropertyName(property)
            companionObject.addProperty(
                PropertySpec.builder(
                    "KEY_${shortName.toSnakeCase()}",
                    PREFERENCES_KEY_TYPE.parameterizedBy(propertyTypeName),
                    KModifier.PRIVATE,
                )
                    .initializer("$initializerName(%S)", shortName)
                    .build()
            )
        }

        return companionObject.build()
    }

    private fun generateConverterGetter(property: KSPropertyDeclaration): String {
        val shortName = getDataStorePropertyName(property)
        val converterName = "${shortName}Converter"
        return "it[KEY_${shortName.toSnakeCase()}]?.let { value -> $converterName.fromString(value) }"
    }

    private fun generateGetter(property: KSPropertyDeclaration): String {
        val shortName = getDataStorePropertyName(property)
        return "it[KEY_${shortName.toSnakeCase()}]"
    }

    private fun generateConverterSetter(property: KSPropertyDeclaration): String {
        val shortName = getDataStorePropertyName(property)
        val converterName = "${shortName}Converter"
        return "it[KEY_${shortName.toSnakeCase()}] = $converterName.toString($shortName)"
    }

    private fun generateSetter(property: KSPropertyDeclaration): String {
        val shortName = getDataStorePropertyName(property)
        return "it[KEY_${shortName.toSnakeCase()}] = $shortName"
    }

    private fun getPropertyTypeName(property: KSPropertyDeclaration): TypeName {
        val typeName = property.type.toTypeName()

        if (typeName in AVAILABLE_TYPES) {
            return typeName
        }

        if (property.annotations.find { it.isTypeOf<TypeConverter>() } == null) {
            throw IllegalStateException(
                "Field `${property.simpleName.asString()}` cannot use $typeName\n" +
                        "Object should be annotated with `@TypeConverter`"
            )
        }

        return STRING
    }

    private fun getPropertyInitializer(propertyTypeName: TypeName) = when (propertyTypeName) {
        INT -> "intPreferencesKey"
        DOUBLE -> "doublePreferencesKey"
        STRING -> "stringPreferencesKey"
        BOOLEAN -> "booleanPreferencesKey"
        FLOAT -> "floatPreferencesKey"
        LONG -> "longPreferencesKey"
        SET.parameterizedBy(STRING) -> "stringSetPreferencesKey"
        else -> throw IllegalStateException(
            "Preferences.Key initializer for $propertyTypeName does not exist",
        )
    }
}