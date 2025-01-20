package com.zellfresh.ui.components.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.zellfresh.client.AddItemToCartMutation
import com.zellfresh.client.CartCountSubscription
import com.zellfresh.client.ListCartsQuery
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow

import javax.inject.Inject

@HiltViewModel
class CartsViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    private val _carts = MutableStateFlow<Result<List<ListCartsQuery.Item>>>(Result.Loading())
    val carts = _carts

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    suspend fun getCarts() {
        val response = apolloClient.query(
            ListCartsQuery()
        ).execute()
        if (response.hasErrors()) {
            _carts.value = Result.Failure(Exception("Failed to get categories"))
        }
        val newProducts = response.data?.cart?.items ?: emptyList()
        val currentProducts = (_carts.value as? Result.Success)?.data.orEmpty()
        _carts.value = Result.Success(newProducts + currentProducts)
    }

    suspend fun getCartCount() {
        apolloClient.subscription(
            CartCountSubscription()
        ).toFlow().collect {
            _cartCount.value = it.data?.cartCount ?: 0
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
                NotificationController.notify(
                    NotificationEvent(
                        message = "Item updated in Cart"
                    )
                )
            }

        }
    }
}