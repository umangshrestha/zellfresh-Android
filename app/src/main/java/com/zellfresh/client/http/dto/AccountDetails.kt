package com.zellfresh.client.http.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
    val sub: String,
    val name: String,
    val email: String,
    val role: String,
    val imageUrl: String,
)