package com.zellfresh.client.http

import android.util.Log
import com.zellfresh.client.http.dto.LoginState
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LoginRepository"

@Singleton
class LoginRepository @Inject constructor(
    private val httpClient: dagger.Lazy<HttpClient>,
    private val tokenRepository: TokenRepository
) {

    suspend fun guestLogin(
        force: Boolean = false
    ): Boolean {
        if (!force && tokenRepository.loginState.firstOrNull() !is LoginState.None) {
            return true
        }
        try {
            val client = httpClient.get()
            val response = client.post("api/auth/guest/login")
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.GuestToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "GuestToken: $e")
        }
        return false
    }

    suspend fun refreshToken(
    ): Boolean {
        val state = tokenRepository.loginState.firstOrNull()
        if (state !is LoginState.AuthToken) {
            return false
        }
        try {
            val client = httpClient.get()
            val response = client.post("api/auth/refresh")
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
                return true
            }
        } catch (e: Exception) {
            tokenRepository.saveLoginState(LoginState.None)
            Log.e(TAG, "requestRefreshToken: $e")
        }
        return false
    }

    suspend fun loginByGoogle(
        idToken: String
    ) {
        try {
            val client = httpClient.get()
            val guestToken = tokenRepository.loginState.firstOrNull()?.getGuestToken()
            val response = client.post("api/auth/google/login") {
                header("Authorization", "Bearer $idToken")
                if (guestToken != null) {
                    header("x-guest-token", guestToken)
                }
            }
            if (response.status.isSuccess()) {
                val value =
                    Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                tokenRepository.saveLoginState(value)
            }
        } catch (e: Exception) {
            Log.e(TAG, "loginByGoogle: ", e)
        }
    }
}