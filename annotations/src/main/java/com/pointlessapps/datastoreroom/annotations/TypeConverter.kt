package com.pointlessapps.datastoreroom.annotations

import com.pointlessapps.datastoreroom.converters.DataStoreTypeConverter
import com.pointlessapps.datastoreroom.converters.GsonDataStoreTypeConverter
import kotlin.reflect.KClass

/**
 * Adds a converter for the field with a type that cannot be serialized automatically.
 * You can use a class with generic type to allow the generator to handle everything:
 *
 * ```
 * class CustomTypeConverter<T> : DataStoreTypeConverter<T> {
 *     fun toString(data: T): String = TODO()
 *     fun fromString(data: String): T = TODO()
 * }
 *
 * @DataStoreEntity
 * class Entity(
 *     @TypeConverter(CustomTypeConverter::class)
 *     val name: Any,
 * )
 * ```
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class TypeConverter(
    val converter: KClass<out DataStoreTypeConverter<*>> = GsonDataStoreTypeConverter::class,
)
