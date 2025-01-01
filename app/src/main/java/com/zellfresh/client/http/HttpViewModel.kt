package com.zellfresh.client.http

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zellfresh.client.http.dto.AccountDetails
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HttpViewModel @Inject constructor(
    private val httpRepository: HttpRepository,
) : ViewModel() {
    private val _accountDetails = MutableStateFlow<AccountDetails?>(null)
    val accountDetails: StateFlow<AccountDetails?> = _accountDetails

    init {
        viewModelScope.launch {
            fetchAccountDetails()
        }
    }

    private suspend fun fetchAccountDetails() {
        val result = httpRepository.getUserDetails()
        if (result.isSuccess) {
            _accountDetails.value = result.getOrNull()
        } else {
            NotificationController.notify(NotificationEvent("Failed to get user details"))
        }
    }
}