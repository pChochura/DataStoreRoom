package com.pointlessapps.datastoreroom

import com.pointlessapps.datastoreroom.annotations.DataStoreEntity
import com.pointlessapps.datastoreroom.annotations.FieldName
import com.pointlessapps.datastoreroom.annotations.TypeConverter

@DataStoreEntity
data class Data(
    @FieldName("nothing")
    private val name: String,
    val value: Set<String>,
    @TypeConverter(JsonSerializerConverter::class)
    val compositeData: List<String>,
)
