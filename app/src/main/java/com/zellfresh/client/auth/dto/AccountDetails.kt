package com.zellfresh.client.auth.dto
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
    val sub: String,
    val name: String,
    val role: String,
    val imageUrl: String,
   )