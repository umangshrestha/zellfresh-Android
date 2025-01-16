package com.zellfresh.client.http

import android.util.Log
import com.zellfresh.client.http.dto.AccountDetails
import com.zellfresh.client.http.dto.LoginState
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "AuthRepository"
private val JsonWithUnknown = Json { ignoreUnknownKeys = true }

@Singleton
class AccountRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val httpClient: HttpClient,
) {
    private val _accountDetails = MutableStateFlow<AccountDetails?>(null)
    val accountDetails: StateFlow<AccountDetails?> = _accountDetails

    init {
        tokenRepository.loginState.onEach {
            if (it == LoginState.None) {
                _accountDetails.value = null
            } else {
                fetchAccountDetails()
            }

        }.launchIn(CoroutineScope(Dispatchers.Default))
    }

    private suspend fun fetchAccountDetails() {
        val result = getUserDetails()
        _accountDetails.value = if (result.isSuccess) {
            result.getOrNull()
        } else {
            null
        }
    }

    private suspend fun getUserDetails(): Result<AccountDetails> {
        try {
            val response = httpClient.get("api/auth/me")
            return if (response.status.isSuccess()) {
                val value = JsonWithUnknown.decodeFromString<AccountDetails>(response.bodyAsText())
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
