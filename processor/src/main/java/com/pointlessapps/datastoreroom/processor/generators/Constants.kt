package com.pointlessapps.datastoreroom.processor.generators

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

val AVAILABLE_TYPES = listOf(
    INT, DOUBLE,
    STRING, BOOLEAN,
    FLOAT, LONG,
    SET.parameterizedBy(STRING),
)
val CONTEXT_TYPE = ClassName("android.content", "Context")
val FLOW_TYPE = ClassName("kotlinx.coroutines.flow", "Flow")
val PREFERENCES_TYPE = ClassName("androidx.datastore.preferences.core", "Preferences")
val PREFERENCES_KEY_TYPE = PREFERENCES_TYPE.nestedClass("Key")
val DATA_STORE_TYPE = ClassName("androidx.datastore.core", "DataStore")
val DATA_STORE_TYPE_CONVERTER_TYPE = ClassName(
    "com.pointlessapps.datastoreroom.converters",
    "DataStoreTypeConverter",
)
