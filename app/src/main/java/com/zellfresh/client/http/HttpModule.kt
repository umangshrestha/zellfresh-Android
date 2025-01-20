package com.zellfresh.client.http

import com.zellfresh.client.Config
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
import kotlinx.coroutines.flow.firstOrNull


@Module
@InstallIn(SingletonComponent::class)
object HttpModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        tokenRepository: TokenRepository, loginRepository: LoginRepository
    ): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(Config.BASE_URL)
                header("Accept", "application/json")
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        tokenRepository.loginState.firstOrNull()?.loadTokens()
                    }
                    refreshTokens {
                        val isTokenRefreshed =
                            loginRepository.refreshToken() || loginRepository.guestLogin()
                        return@refreshTokens if (isTokenRefreshed) tokenRepository.loginState.firstOrNull()
                            ?.loadTokens()
                        else null

                    }
                }
            }
            engine {
                config {
                    followRedirects(true)
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
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