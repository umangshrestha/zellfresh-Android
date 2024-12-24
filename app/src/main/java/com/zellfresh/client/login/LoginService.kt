package com.zellfresh.client.login

import com.zellfresh.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.header
import androidx.lifecycle.ViewModel
import com.zellfresh.client.login.dto.AccountDetails
import com.zellfresh.client.login.dto.LoginState
import com.zellfresh.utils.LoginStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.ktor.client.statement.bodyAsText
import io.ktor.http.path
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class LoginService(
    private val client: HttpClient,
    private val loginStorage: LoginStorage
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(
        runBlocking {
            loginStorage.getLoginState()
        }
    )
    private val loginState: StateFlow<LoginState> = _loginState

    private val _accountDetails = MutableStateFlow<AccountDetails?>(null)
    val accountDetails: StateFlow<AccountDetails?> = _accountDetails

    private suspend fun updateLoginState(loginState: LoginState) {
        _loginState.value = loginState
        loginStorage.saveLoginState(loginState)
    }

    suspend fun guestLogin() {
        return client.post {
            url("${BuildConfig.API_URL}/login/guest")
        }.let {
            if (it.status.value == 200) {
                val value = Json.decodeFromString<LoginState>(it.bodyAsText())
                updateLoginState(value)
            }
        }
    }

    private suspend fun refreshToken(): Boolean {
        when (val state = loginState.value) {
            is LoginState.AuthToken -> {
                client.post(BuildConfig.API_URL) {
                    url {
                        path("/login/refresh")
                    }
                    header("Authorization", "Bearer ${state.refreshToken}")
                }.let {
                    if (it.status.value == 200) {
                        val value = Json.decodeFromString<LoginState>(it.bodyAsText())
                        updateLoginState(value)
                    }
                }
            }

            else -> {
                updateLoginState(LoginState.None)
            }
        }
        return _loginState.value !is LoginState.None
    }


    suspend fun loginByGoogle(idToken: String) {
        return client.post(BuildConfig.API_URL) {
            url {
                path("auth//login/google")
            }
            header("Authorization", "Bearer $idToken")
            (loginState.value as? LoginState.GuestToken)?.let {
                header("x-guest-token", it.guestToken)
            }
        }.let {
            if (it.status.value == 200) {
                _loginState.value = Json.decodeFromString<LoginState>(it.bodyAsText())
            }
        }
    }

    suspend fun updateUserDetails(retry: Boolean = true) {
        when (val state = loginState.value) {
            is LoginState.None -> {
                guestLogin()
                return updateUserDetails(retry)
            }

            is LoginState.AuthToken -> {
                client.get(BuildConfig.API_URL) {
                    url {
                        path("/auth/me")
                    }
                    header("Authorization", "Bearer ${state.accessToken}")
                }.let {
                    if (it.status.value == 200) {
                        _accountDetails.value =
                            Json.decodeFromString<AccountDetails>(it.bodyAsText())
                    } else if (it.status.value == 401 && retry) {
                        val success = refreshToken()
                        if (success) {
                            return updateUserDetails(false)
                        } else {
                            _loginState.value = LoginState.None
                            return updateUserDetails(retry)
                        }
                    }

                }
            }

            is LoginState.GuestToken -> client.get(BuildConfig.API_URL) {
                url {
                    path("/auth/me")
                }
                header("Authorization", "Bearer ${state.guestToken}")
            }.let {
                if (it.status.value == 200) {
                    _accountDetails.value =
                        Json.decodeFromString<AccountDetails>(it.bodyAsText())
                } else if (it.status.value == 401 && retry) {
                    val success = refreshToken()
                    if (success) {
                        return updateUserDetails(false)
                    } else {
                        _loginState.value = LoginState.None
                        return updateUserDetails(retry)
                    }
                }

            }
        }
    }
}




