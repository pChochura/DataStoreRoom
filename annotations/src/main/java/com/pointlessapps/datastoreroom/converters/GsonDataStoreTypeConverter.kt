package com.pointlessapps.datastoreroom.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A default [DataStoreTypeConverter] that serializes data using [Gson] library.
 */
class GsonDataStoreTypeConverter<T> : DataStoreTypeConverter<T> {
    private val gson = Gson()

    override fun toString(data: T): String = gson.toJson(data)
    override fun fromString(data: String): T = gson.fromJson(data, object : TypeToken<T>() {}.type)
}