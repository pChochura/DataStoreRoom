package com.pointlessapps.datastoreroom

fun main() {
    val data = Data(
        name = "a",
        value = emptySet(),
        compositeData = emptyList(),
    )

    println(data)
}