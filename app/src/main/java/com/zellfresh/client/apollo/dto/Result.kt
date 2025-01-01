package com.zellfresh.client.apollo.dto

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    data class Loading(val message: String = "Loading...") : Result<Nothing>()
}