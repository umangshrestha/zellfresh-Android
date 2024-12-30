package com.zellfresh.client

import com.zellfresh.client.auth.AuthRepository
import com.zellfresh.ui.store.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
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


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        dataStoreRepository: DataStoreRepository,
        authRepository: AuthRepository
    ): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(Config.BASE_URL)
                header("Accept", "application/json")
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        dataStoreRepository.loadTokens()
                    }
                    refreshTokens {
                        authRepository.refreshToken(client)
                            ?: authRepository.guestLogin(client)
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