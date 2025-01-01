package com.zellfresh.client.http.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
@Serializable
sealed class LoginState {
    @Serializable
    @SerialName("AuthToken")
    data class AuthToken(
        val accessToken: String,
        val refreshToken: String
    ) : LoginState()

    @Serializable
    @SerialName("GuestToken")
    data class GuestToken(
        val guestToken: String
    ) : LoginState()

    @Serializable
    @SerialName("None")
    data object None: LoginState()
}
