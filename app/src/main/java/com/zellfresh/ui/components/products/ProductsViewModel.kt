package com.zellfresh.ui.components.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.zellfresh.client.ListProductsQuery
import com.zellfresh.client.ListProductsQuery.Item as Product
import com.zellfresh.client.apollo.dto.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    private val _productsState = MutableStateFlow<Result<List<Product>>>(Result.Loading())
    val productsState = _productsState
    private var cursor: String? = null

    suspend fun getProducts(category: String? = null, reset: Boolean = false) {
        if (reset) {
            cursor = null
            _productsState.value = Result.Loading()
        }
        viewModelScope.launch {
            val response = apolloClient.query(
                ListProductsQuery(
                    category = Optional.presentIfNotNull(category),
                    cursor = Optional.presentIfNotNull(cursor),
                )
            ).execute()
            if (response.hasErrors()) {
                _productsState.value = Result.Failure(Exception("Failed to get categories"))
            }
            cursor = response.data?.products?.pagination?.next
            val newProducts = response.data?.products?.items ?: emptyList()
            val currentProducts = (_productsState.value as? Result.Success)?.data.orEmpty()
            _productsState.value = Result.Success(newProducts + currentProducts)
        }
    }
}