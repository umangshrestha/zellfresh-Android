package com.zellfresh.client.auth

import com.zellfresh.client.auth.dto.LoginState
import com.zellfresh.ui.store.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        dataStoreRepository: DataStoreRepository
    ): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url("https://www.zellfresh.com")
                header("Accept", "application/json")
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        when (val state = dataStoreRepository.loginState.firstOrNull()) {
                            is LoginState.AuthToken ->
                                BearerTokens(state.accessToken, state.refreshToken)

                            is LoginState.GuestToken ->
                                BearerTokens(state.guestToken, "")

                            else ->
                                null
                        }
                    }
                    refreshTokens {
                        val state = dataStoreRepository.loginState.firstOrNull()
                        if (state !is LoginState.AuthToken) {
                            return@refreshTokens null
                        }
                        try {
                            val response = client.post("auth/login/google") {
                                header("Authorization", "Bearer ${state.refreshToken}")
                            }
                            if (response.status.isSuccess()) {
                                val value =
                                    Json.decodeFromString<LoginState.AuthToken>(response.bodyAsText())
                                dataStoreRepository.saveLoginState(value)
                                return@refreshTokens BearerTokens(
                                    value.accessToken,
                                    value.refreshToken
                                )
                            }
                        } catch (e: Exception) {
                            return@refreshTokens null
                        }
                        return@refreshTokens null
                    }
                }
            }
            engine {
                config {
                    followRedirects(true)
                    connectTimeout(1, TimeUnit.SECONDS)
                    readTimeout(1, TimeUnit.SECONDS)
                    retryOnConnectionFailure(true)
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(3)
                exponentialDelay()
                maxRetries = 3
                delayMillis { retry ->
                    retry * 3000L
                }
                retryIf { _, response ->
                    !response.status.isSuccess()
                }
            }
        }
    }
}