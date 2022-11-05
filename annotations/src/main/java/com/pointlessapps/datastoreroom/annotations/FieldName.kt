package com.pointlessapps.datastoreroom.annotations

/**
 * Changes the name of the field when generating methods:
 * ```
 * get{value}
 * update{value}
 * getLastOrNull{value}
 * remove{value}
 * ```
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class FieldName(val value: String)
