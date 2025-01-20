package com.zellfresh.client.http.dto

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

interface Token {
    fun getAccessToken(): String? = null
    fun loadTokens(): BearerTokens? = null
    fun getGuestToken(): String? = null
}

@Polymorphic
@Serializable
sealed class LoginState : Token {
    @Serializable
    @SerialName("AuthToken")
    data class AuthToken(
        private val accessToken: String,
        private val refreshToken: String
    ) : LoginState(), Token {
        override fun loadTokens(): BearerTokens = BearerTokens(accessToken, refreshToken)
    }

    @Serializable
    @SerialName("GuestToken")
    data class GuestToken(
        private val guestToken: String
    ) : LoginState(), Token {
        override fun getAccessToken(): String = guestToken
        override fun loadTokens(): BearerTokens = BearerTokens(guestToken, null)
        override fun getGuestToken(): String = guestToken
    }

    @Serializable
    @SerialName("None")
    data object None : LoginState(), Token
}
