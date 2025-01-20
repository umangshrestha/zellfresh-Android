package com.zellfresh.client.http

import androidx.lifecycle.ViewModel
import com.zellfresh.client.http.dto.AccountDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
  accountRepository: AccountRepository
): ViewModel() {
   val accountDetails: StateFlow<AccountDetails?> = accountRepository.accountDetails
}