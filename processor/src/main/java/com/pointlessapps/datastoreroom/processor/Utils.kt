package com.pointlessapps.datastoreroom.processor

import com.google.common.base.CaseFormat
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.pointlessapps.datastoreroom.annotations.DataStoreEntity
import com.pointlessapps.datastoreroom.annotations.FieldName
import com.pointlessapps.datastoreroom.annotations.TypeConverter

internal inline fun <reified T> KSAnnotation.isTypeOf() =
    annotationType.resolve().declaration.qualifiedName?.asString() == T::class.qualifiedName

internal fun String.startWithUppercase(): String = this.replaceFirstChar { it.uppercase() }

internal fun String.toSnakeCase(): String =
    CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert(this).orEmpty()

internal fun getDataStoreEntityName(classDeclaration: KSClassDeclaration): String {
    val annotationName = classDeclaration.annotations
        .find { it.isTypeOf<DataStoreEntity>() }
        ?.arguments
        ?.find { it.name?.asString() == "name" }

    return ((annotationName?.value as? String)?.takeIf { it.isNotEmpty() }
        ?: classDeclaration.simpleName.getShortName()).startWithUppercase()
}

internal fun getDataStorePropertyName(propertyDeclaration: KSPropertyDeclaration): String {
    val annotationName = propertyDeclaration.annotations
        .find { it.isTypeOf<FieldName>() }
        ?.arguments
        ?.find { it.name?.asString() == "value" }

    return (annotationName?.value as? String)?.takeIf { it.isNotBlank() }
        ?: propertyDeclaration.simpleName.getShortName()
}

internal fun shouldDataStorePropertyBeConverted(propertyDeclaration: KSPropertyDeclaration) =
    propertyDeclaration.annotations.find { it.isTypeOf<TypeConverter>() } != null
