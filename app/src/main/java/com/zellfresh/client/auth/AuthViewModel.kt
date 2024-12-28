package com.zellfresh.client.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zellfresh.client.auth.dto.AccountDetails
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _accountDetails = MutableStateFlow<AccountDetails?>(null)
    val accountDetails: StateFlow<AccountDetails?> = _accountDetails

    fun init() {
        viewModelScope.launch {
            val result = authRepository.getUserDetails()
            if (result.isSuccess) {
                _accountDetails.value = result.getOrNull()
                NotificationController.notify(NotificationEvent("Logged in successfully"))
            } else {
                NotificationController.notify(NotificationEvent("Failed to login"))
            }
        }
    }
}