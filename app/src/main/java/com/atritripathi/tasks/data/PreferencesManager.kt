package com.atritripathi.tasks.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

enum class SortOrder { BY_NAME, BY_DATE }

data class UserPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")
    private val dataStore = context.dataStore

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            } else throw exception
        }.map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted = preferences[HIDE_COMPLETED_KEY] ?: false
            UserPreferences(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[HIDE_COMPLETED_KEY] = hideCompleted
        }
    }

    private companion object {
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED_KEY = booleanPreferencesKey("hide_completed")
    }
}