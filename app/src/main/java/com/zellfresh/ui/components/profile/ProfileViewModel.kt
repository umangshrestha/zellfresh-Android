package com.zellfresh.ui.components.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.zellfresh.client.PutAddressMutation
import com.zellfresh.client.apollo.dto.Result
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.apollographql.apollo.api.Optional
import com.zellfresh.client.ProfileQuery
import com.zellfresh.client.PutUserMutation
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    private var addressId: String? = null

    private val _profile = MutableStateFlow<Result<ProfileQuery.Me>>(Result.Loading())
    val profile = _profile

    suspend fun getProfile() {
        val response = apolloClient.query(ProfileQuery()).execute()
        val result = response.data?.me ?: ProfileQuery.Me(
            email = "",
            name = "",
            phone = "",
            defaultAddress = null
        )
        if (result.defaultAddress != null) {
            addressId = result.defaultAddress.addressId
        }
        _profile.value = Result.Success(result)
    }

    fun putAddress(
        apt: String?,
        street: String,
        zip: String,
        additionalInfo: String?,
        city: String = "Bengaluru",
        state: String = "Karnataka",
        country: String = "India"
    ) {
        viewModelScope.launch {
            val response = apolloClient.mutation(
                PutAddressMutation(
                    addressId = Optional.Present(addressId),
                    apt = Optional.Present(apt),
                    city = city,
                    street = street,
                    state = state,
                    zip = zip,
                    country = country,
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

    fun putUser(
        name: String,
        email: String,
        phone: String
    ) {
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