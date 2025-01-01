package com.zellfresh.client.http

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.zellfresh.client.http.dto.AccountDetails
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import javax.inject.Inject
import android.util.Log
import javax.inject.Singleton

private const val TAG = "AuthRepository"
private val JsonWithUnknown = Json { ignoreUnknownKeys = true }

@Singleton
class HttpRepository @Inject constructor(
    private val httpClient: HttpClient,
) {
    suspend fun getUserDetails(): Result<AccountDetails> {
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




