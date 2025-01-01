package com.zellfresh.client.http

import android.util.Log
import com.zellfresh.client.http.dto.LoginState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LoginRepository"

@Singleton
class LoginRepository @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private val loginState: Flow<LoginState> = tokenRepository.loginState

    suspend fun guestLogin(
        client: HttpClient,
        force: Boolean = false
    ): BearerTokens? {
        if (!force && loginState.firstOrNull() !is LoginState.None) {
            return tokenRepository.loadTokens()
        }
        try {
            val response = client.post("api/auth/guest/login")
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.GuestToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
                return BearerTokens(value.guestToken, "")
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    suspend fun refreshToken(
        client: HttpClient
    ): BearerTokens? {
        val state = loginState.firstOrNull()
        if (state !is LoginState.AuthToken) {
            return null
        }
        try {
            val response = client.post("api/auth/refresh")
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
                return BearerTokens(value.accessToken, value.refreshToken)
            }
        } catch (e: Exception) {
            Log.e(TAG, "requestRefreshToken: $e")
        }
        return null
    }

    suspend fun loginByGoogle(
        client: HttpClient,
        idToken: String
    ): BearerTokens? {
        try {
            val guestToken = loginState.firstOrNull() ?: LoginState.None
            val response = client.post("api/auth/google/login") {
                header("Authorization", "Bearer $idToken")
                if (guestToken is LoginState.GuestToken) {
                    header("x-guest-token", guestToken.guestToken)
                }
            }
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
                return BearerTokens(value.accessToken, value.refreshToken)
            }
        } catch (e: Exception) {
            Log.e(TAG, "loginByGoogle: ", e)
        }
        return null
    }
}