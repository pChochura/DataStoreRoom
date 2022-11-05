package com.pointlessapps.datastoreroom

import com.pointlessapps.datastoreroom.converters.DataStoreTypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonSerializerConverter : DataStoreTypeConverter<List<String>> {

    override fun toString(data: List<String>): String {
        return Json.encodeToString(data)
    }

    override fun fromString(data: String): List<String> {
        return Json.decodeFromString(data)
    }

}
