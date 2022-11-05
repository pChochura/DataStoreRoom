package com.pointlessapps.datastoreroom

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map

class ExampleData(private val context: Context) {

    private companion object {
        val KEY_NAME: Preferences.Key<String> = stringPreferencesKey("name")
        val KEY_VALUE = floatPreferencesKey("value")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(javaClass.simpleName)

    suspend fun getLastNameOrNull(): String? {
        return context.dataStore.data.map {
            it[KEY_NAME]
        }.lastOrNull()
    }

    fun getName(): Flow<String?> {
        return context.dataStore.data.map {
            it[KEY_NAME]
        }
    }

    suspend fun updateName(name: String) {
        context.dataStore.edit {
            it[KEY_NAME] = name
        }
    }

    suspend fun removeName() {
        context.dataStore.edit { it.remove(KEY_NAME) }
    }

    suspend fun getLastValueOrNull(): Float? {
        return context.dataStore.data.map {
            it[KEY_VALUE]
        }.lastOrNull()
    }

    fun getValue(): Flow<Float?> {
        return context.dataStore.data.map {
            it[KEY_VALUE]
        }
    }

    suspend fun updateValue(value: Float) {
        context.dataStore.edit {
            it[KEY_VALUE] = value
        }
    }
}