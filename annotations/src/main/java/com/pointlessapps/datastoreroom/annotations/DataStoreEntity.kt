package com.pointlessapps.datastoreroom.annotations

/**
 * Marks the current class as an entity for the DataStore mechanism
 * generating a `DataStore{class_name}` class with all of the functionality.
 *
 * Example:
 * ```
 * @DataStoreEntity(name = "EntityClass")
 * class Entity(
 *     val name: String,
 *     @FieldName("counter")
 *     val counterNumber: Int,
 * )
 * ```
 * Code from above would generate a class named `DataStoreEntityClass`
 * that would contain methods for the `name` field:
 * ```
 * val dataStoreEntity = DataStoreEntityClass()
 * val nameFlow: Flow<String> = dataStoreEntity.getName()
 * val lastName: String? = runBlocking { dataStoreEntity.getLastOrNullName() }
 * runBlocking { dataStoreEntity.updateName("newName") }
 * runBlocking { dataStoreEntity.removeName() }
 * ```
 * For the field `counterNumber` that has a [FieldName] annotation with a value of "counter"
 * there would be generated a set of methods for the name "counter":
 * ```
 * val counterFlow: Flow<String> = dataStoreEntity.getCounter()
 * val lastCounter: String? = runBlocking { dataStoreEntity.getLastOrNullCounter() }
 * runBlocking { dataStoreEntity.updateCounter(2) }
 * runBlocking { dataStoreEntity.removeCounter() }
 * ```
 *
 * Omitting the [name] argument would result in using the name of the class instead.
 * For the above example without a [name] argument it would look like that:
 * ```
 * @DataStoreEntity
 * class Entity(
 *     val name: String,
 *     @FieldName("counter")
 *     val counterNumber: Int,
 * )
 * val dataStoreEntity = DataStoreEntity()
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DataStoreEntity(val name: String = "")
