package com.zellfresh.client.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.header
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
import javax.inject.Singleton

private const val TAG = "AuthRepository"
private val JsonWithUnknown = Json { ignoreUnknownKeys = true }

@Singleton
class AuthRepository @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    private val loginState = dataStoreRepository.loginState

    suspend fun guestLogin(
        client: HttpClient
    ): BearerTokens? {
        try {
            val response = client.post("api/auth/guest/login")
            if (response.status.isSuccess()) {
                val value = JsonWithUnknown.decodeFromString<LoginState.GuestToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
                return BearerTokens(value.guestToken, "")
            }
        } catch (e: Exception) {
            Log.e(TAG, "guestLogin: $e")
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
                val value = JsonWithUnknown.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
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
                val value = JsonWithUnknown.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                dataStoreRepository.saveLoginState(value)
                return BearerTokens(value.accessToken, value.refreshToken)
            }
        } catch (e: Exception) {
            Log.e(TAG, "loginByGoogle: ", e)
        }
        return null
    }

    suspend fun getUserDetails(
        client: HttpClient
    ): Result<AccountDetails> {
        try {
            val response = client.get("api/auth/me")
            return if (response.status.isSuccess()) {
                val value = JsonWithUnknown.decodeFromString<AccountDetails>(response.bodyAsText(),)
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




