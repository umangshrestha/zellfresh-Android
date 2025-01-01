package com.zellfresh.client.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.zellfresh.client.http.dto.LoginState
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val LOGIN_STATE_KEY = "login_state"

@Singleton
class TokenRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _loginStateKey = stringPreferencesKey(name = LOGIN_STATE_KEY)
    val loginState: Flow<LoginState> = dataStore.data.map { preferences ->
        preferences[_loginStateKey]?.let { Json.decodeFromString<LoginState>(it) }
            ?: LoginState.None
    }

    suspend fun saveLoginState(loginState: LoginState) {
        dataStore.edit { it[_loginStateKey] = Json.encodeToString(loginState) }
    }

    suspend fun loadTokens(): BearerTokens? {
        return when (val state = loginState.firstOrNull()) {
            is LoginState.AuthToken -> BearerTokens(state.accessToken, state.refreshToken)
            is LoginState.GuestToken -> BearerTokens(state.guestToken, "")
            else -> null
        }
    }

    suspend fun getAccessToken(): String? {
        return when (val state = loginState.firstOrNull()) {
            is LoginState.AuthToken -> state.accessToken
            is LoginState.GuestToken -> state.guestToken
            else -> null
        }
    }
}