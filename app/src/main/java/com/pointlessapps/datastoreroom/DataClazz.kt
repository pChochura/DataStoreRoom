package com.pointlessapps.datastoreroom

@DataStoreEntity(name = "Data")
data class DataClazz(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: Float,
)
