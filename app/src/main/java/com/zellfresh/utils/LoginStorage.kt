package com.zellfresh.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.zellfresh.client.login.dto.LoginState
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class LoginStorage(
    private val context: DataStore<Preferences>
) {
    private val _loginStateKey = stringPreferencesKey("login_state")

    suspend fun saveLoginState(loginState: LoginState) {
        context.edit {
            it[_loginStateKey] = Json.encodeToString(loginState)
        }
    }

    suspend fun getLoginState(): LoginState {
        return context.data.first()[_loginStateKey]?.let {
            Json.decodeFromString<LoginState>(it)
        } ?: LoginState.None
    }

}