package com.zellfresh.ui.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val THEME_PREFERENCES_KEY = "is_dark_theme"

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _isDarkTheme = booleanPreferencesKey(name = THEME_PREFERENCES_KEY)
    val isDarkTheme = dataStore.data.map { preferences -> preferences[_isDarkTheme] ?: false }

    suspend fun setTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[_isDarkTheme] = isDarkTheme
        }
    }
}