package com.zellfresh.client.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.header
import androidx.lifecycle.ViewModel
import com.zellfresh.client.auth.dto.AccountDetails
import com.zellfresh.client.auth.dto.LoginState
import com.zellfresh.ui.store.DataStoreRepository
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Inject
import android.util.Log
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import io.ktor.client.statement.request
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val client: HttpClient,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val TAG = "AuthRepository"
    private val loginState = dataStoreRepository.loginState

    private suspend fun guestLogin(): BearerTokens? {
        try {
            Log.d(TAG, "guestLogin: ")
            val response = client.post("api/auth/guest/login")
            Log.e(TAG, "guestLogin: ${response.request.url}")
            Log.e(TAG, "guestLogin: ${response.bodyAsText()}")
            Log.e(TAG, "guestLogin: ${response.status}")
            if (response.status.isSuccess()) {
                val value = Json.decodeFromString<LoginState.GuestToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
                return BearerTokens(value.guestToken, "")
            }
        } catch (e: Exception) {
            Log.e(TAG, "guestLogin: $e")
            return null
        }
        return null
    }

    suspend fun refreshToken(): BearerTokens? {
        when (val state = loginState.firstOrNull()) {
            is LoginState.AuthToken -> {
                return requestRefreshToken(state.refreshToken)
            }

            else -> {
                dataStoreRepository.saveLoginState(LoginState.None)
                return null
            }
        }
    }

    private suspend fun requestRefreshToken(refreshToken: String): BearerTokens? {
        try {
            val response = client.post("api/auth/refresh") {
                header("Authorization", "Bearer $refreshToken")
            }
            Log.e(TAG, "requestRefreshToken: ${response.bodyAsText()}")
            Log.e(TAG, "requestRefreshToken: ${response.status}")
            if (response.status.isSuccess()) {
                val value = Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
                return BearerTokens(value.accessToken, value.refreshToken)
            }
        } catch (e: Exception) {
            Log.e(TAG, "requestRefreshToken: $e")
            return guestLogin()
        }
        return null
    }

    suspend fun loginByGoogle(idToken: String): Boolean {
        try {
            val guestToken = loginState.firstOrNull() ?: LoginState.None
            val response = client.post("api/auth/google/login") {
                header("Authorization", "Bearer $idToken")
                if (guestToken is LoginState.GuestToken) {
                    header("x-guest-token", guestToken.guestToken)
                }
            }
            Log.e(TAG, "loginByGoogle: ${response.bodyAsText()}")
            Log.e(TAG, "loginByGoogle: ${response.status}")
            if (response.status.isSuccess()) {
                val value = Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "loginByGoogle: ", e)
            return false
        }
        return false
    }

    suspend fun getUserDetails(): Result<AccountDetails> {
        val loginState = loginState.firstOrNull() ?: LoginState.None
        if (loginState is LoginState.None) {
            Log.d(TAG, "No guest token found: None")
            guestLogin()
        }
        try {
            Log.d(TAG, "getUserDetails: ")
            val response = client.get("api/auth/me")
            Log.e(TAG, "getUserDetails: ${response.bodyAsText()}")
            Log.e(TAG, "getUserDetails: ${response.status}")
            return if (response.status.isSuccess()) {
                val value = Json.decodeFromString<AccountDetails>(response.bodyAsText())
                Result.success(value)
            } else {
                Result.failure(Exception("Failed to get user details"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserDetails: $e")
            return Result.failure(e)
        }
    }
}




