package com.zellfresh.ui.components.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.zellfresh.client.AddItemToCartMutation
import com.zellfresh.client.ClearCartMutation
import com.zellfresh.client.ListCartsQuery
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class CartsViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    private val _carts = MutableStateFlow<Result<List<ListCartsQuery.Item>>>(Result.Loading())
    val carts = _carts

    private val _cartCount = MutableStateFlow(0)
    val cartCount = _cartCount

    private val _checkoutDetails = MutableStateFlow(
        ListCartsQuery.CheckoutDetails(
            0.0,
            false
        )
    )

    val checkoutDetails = _checkoutDetails

    suspend fun getCarts() {
        val response = apolloClient.query(
            ListCartsQuery()
        ).execute()
        if (response.hasErrors()) {
            _carts.value = Result.Failure(Exception("Failed to get categories"))
        } else {
            val newProducts = response.data?.cart?.items ?: emptyList()
            _cartCount.value = response.data?.cart?.count ?: 0
            _checkoutDetails.value =
                response.data?.cart?.checkoutDetails ?: ListCartsQuery.CheckoutDetails(
                    0.0,
                    false
                )
            _carts.value = Result.Success(newProducts)
        }
    }

    fun addItemToCart(productId: String, quantity: Int) {
        viewModelScope.launch {
            val response = apolloClient.mutation(
                AddItemToCartMutation(
                    productId = productId,
                    quantity = quantity
                )
            ).execute()
            if (response.hasErrors()) {
                NotificationController.notify(
                    NotificationEvent(
                        message = response.toString()
                    )
                )
            } else {
                _cartCount.value = response.data?.addItemToCart?.count ?: 0
                NotificationController.notify(
                    NotificationEvent(
                        message = "Item updated in Cart"
                    )
                )
            }
            getCarts()
        }
    }


    fun clearCart() {
        viewModelScope.launch {
            val response = apolloClient.mutation(
                ClearCartMutation()
            ).execute()
            if (response.hasErrors()) {
                NotificationController.notify(
                    NotificationEvent(
                        message = response.toString()
                    )
                )
            } else {
                _cartCount.value = response.data?.clearCart?.count ?: 0
                NotificationController.notify(
                    NotificationEvent(
                        message = "Item updated in Cart"
                    )
                )
            }
            getCarts()
        }
    }
}