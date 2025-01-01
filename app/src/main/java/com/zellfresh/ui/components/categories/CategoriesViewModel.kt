package com.zellfresh.ui.components.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.zellfresh.client.ListCategoriesQuery
import com.zellfresh.client.ListCategoriesQuery.Category
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {
    private val _categoriesState = MutableStateFlow<Result<List<Category>>>(Result.Loading())
    val categoriesState = _categoriesState

    init {
        viewModelScope.launch {
            getCategories()
        }
    }

    private suspend fun getCategories() {
        try {
            val response = apolloClient.query(ListCategoriesQuery()).execute()
            if (response.hasErrors()) {
                _categoriesState.value = Result.Failure(Exception("Failed to get categories"))
            }
            _categoriesState.value = Result.Success(response.data?.categories ?: emptyList())
        } catch (e: Exception) {
            _categoriesState.value = Result.Failure(e)
            NotificationController.notify(NotificationEvent("Failed to get categories"))
        }
    }
}