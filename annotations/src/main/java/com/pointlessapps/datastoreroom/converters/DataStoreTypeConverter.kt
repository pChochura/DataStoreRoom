package com.pointlessapps.datastoreroom.converters

interface DataStoreTypeConverter<T> {
    /**
     * Converts an object [data] to a string to allow saving it to the DataStore
     */
    fun toString(data: T): String

    /**
     * Converts the string [data] stored inside of the DataStore to an object
     */
    fun fromString(data: String): T
}