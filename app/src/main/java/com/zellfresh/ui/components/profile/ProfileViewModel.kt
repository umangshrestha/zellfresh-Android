package com.zellfresh.ui.components.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.zellfresh.client.PutAddressMutation
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.apollographql.apollo.api.Optional
import com.zellfresh.client.ProfileQuery
import com.zellfresh.client.PutUserMutation
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    private val _profile = MutableStateFlow<ProfileQuery.Me?>(null)
    val profile = _profile

    suspend fun getProfile() {
        val response = apolloClient.query(ProfileQuery()).execute()
        val result = response.data?.me
        if (result?.defaultAddress != null) {
            addressId = result.defaultAddress.addressId
        }

        _profile.value = result
    }

    /* Address */
    var addressId by mutableStateOf<String?>(null)
    var apt by mutableStateOf("")
    var street by mutableStateOf("")
    var zip by mutableStateOf("")
    var additionalInfo by mutableStateOf("")

    fun putAddress() {
        viewModelScope.launch {

            val response = apolloClient.mutation(
                PutAddressMutation(
                    addressId = Optional.Present(addressId),
                    apt = Optional.Present(apt),
                    street = street,
                    zip = zip,
                    city = "Bengaluru",
                    state = "Karnataka",
                    country = "India",
                    additionalInfo = Optional.Present(additionalInfo),
                )
            ).execute()
            NotificationController.notify(
                NotificationEvent(
                    if (response.hasErrors()) "Failed to update address" else "Address updated"
                )
            )
        }
    }

    /* User */
    var name by mutableStateOf<String>("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")

    fun putUser() {
        viewModelScope.launch {
            val response = apolloClient.mutation(
                PutUserMutation(
                    name = name,
                    email = email,
                    phone = phone
                )
            ).execute()
            NotificationController.notify(
                NotificationEvent(
                    if (response.hasErrors()) "Failed to update user details" else "User details updated"
                )
            )
        }
    }
}