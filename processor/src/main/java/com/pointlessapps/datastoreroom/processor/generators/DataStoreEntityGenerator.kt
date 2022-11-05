package com.pointlessapps.datastoreroom.processor.generators

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.pointlessapps.datastoreroom.processor.getDataStoreEntityName
import com.pointlessapps.datastoreroom.processor.isTypeOf
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo

internal class DataStoreEntityGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(classDeclaration: KSClassDeclaration) {
        val filename = "DataStore${getDataStoreEntityName(classDeclaration)}"

        val file = FileSpec.builder(
            packageName = classDeclaration.packageName.asString(),
            fileName = filename,
        )

        val mainClass = TypeSpec.classBuilder(filename)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("context", CONTEXT_TYPE)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("context", CONTEXT_TYPE, KModifier.PRIVATE)
                    .initializer("context")
                    .build()
            )
            .addProperty(generateDataStoreProperty(file))

        val properties = classDeclaration.getAllProperties().filter { property ->
            property.annotations.none { it.isTypeOf<Transient>() } && property.validate()
        }

        if (properties.iterator().hasNext()) {
            val propertyGenerator = DataStorePropertyGenerator(file)

            mainClass.addProperties(propertyGenerator.generateTypeConverters(properties))
            mainClass.addType(propertyGenerator.generateCompanionObject(properties))
            mainClass.addFunctions(propertyGenerator.generateUpdateMethods(properties))
            mainClass.addFunctions(propertyGenerator.generateRemoveMethods(properties))
            mainClass.addFunctions(propertyGenerator.generateGetMethods(properties))
            mainClass.addFunctions(propertyGenerator.generateGetLastOrNullMethods(properties))
        }

        file.addType(mainClass.build())

        file.build().writeTo(
            codeGenerator = codeGenerator,
            dependencies = Dependencies(false),
        )
    }

    private fun generateDataStoreProperty(file: FileSpec.Builder): PropertySpec {
        file.addImport("androidx.datastore.preferences", "preferencesDataStore")

        return PropertySpec.builder(
            "dataStore",
            DATA_STORE_TYPE.parameterizedBy(PREFERENCES_TYPE),
        ).receiver(CONTEXT_TYPE)
            .addModifiers(KModifier.PRIVATE)
            .delegate("preferencesDataStore(javaClass.simpleName)")
            .build()
    }
}