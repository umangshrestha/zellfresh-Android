package com.zellfresh.client.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zellfresh.client.auth.dto.AccountDetails
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val client: HttpClient,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _accountDetails = MutableStateFlow<AccountDetails?>(null)
    val accountDetails: StateFlow<AccountDetails?> = _accountDetails

    fun init() {
        viewModelScope.launch {
            authRepository.guestLogin(client)
            val result = authRepository.getUserDetails(client)
            if (result.isSuccess) {
                _accountDetails.value = result.getOrNull()
            } else {
                NotificationController.notify(NotificationEvent("Failed to get user details"))
            }
        }
    }
}