package com.zellfresh.client.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.zellfresh.client.http.dto.AccountDetails
import com.zellfresh.client.http.dto.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val LOGIN_STATE_KEY = "login_state"

@Singleton
class TokenRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val _loginStateKey = stringPreferencesKey(name = LOGIN_STATE_KEY)
    val loginState: Flow<LoginState> = dataStore.data.map { preferences ->
        preferences[_loginStateKey]?.let {
            Json.decodeFromString<LoginState>(it)
        } ?: LoginState.None
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginState.None
    )

    suspend fun saveLoginState(loginState: LoginState) {
        dataStore.edit { it[_loginStateKey] = Json.encodeToString(loginState) }
    }
}



