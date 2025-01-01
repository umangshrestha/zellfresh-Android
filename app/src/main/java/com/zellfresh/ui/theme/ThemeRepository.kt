package com.zellfresh.ui.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private const val THEME_PREFERENCES_KEY = "is_dark_theme"

@Singleton
class ThemeRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _isDarkTheme = booleanPreferencesKey(name = THEME_PREFERENCES_KEY)
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { it[_isDarkTheme] ?: false }

    suspend fun setTheme(isDarkTheme: Boolean) {
        dataStore.edit { it[_isDarkTheme] = isDarkTheme }
    }
}