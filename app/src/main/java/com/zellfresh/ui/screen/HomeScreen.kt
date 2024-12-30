package com.zellfresh.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.zellfresh.client.ListCategoriesQuery
import com.zellfresh.client.apolloClient
import com.zellfresh.ui.components.ImageButton
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var categoriesList by remember {
        mutableStateOf(
            emptyList<ListCategoriesQuery.Category>()
        )
    }
    LaunchedEffect(Unit) {
        try {
            val response = apolloClient.query(ListCategoriesQuery()).execute()
            if (response.hasErrors() || response.data == null) {
                scope.launch {
                    NotificationController.notify(
                        NotificationEvent(
                            message = response.errors?.get(0)?.message ?: "Something went wrong",
                            actionLabel = "Retry"
                        )
                    )
                }
            }
            categoriesList = response.data?.categories ?: emptyList()
        } catch (e: Exception) {
            scope.launch {
                NotificationController.notify(
                    NotificationEvent(
                        message = e.message ?: "Something went wrong",
                        actionLabel = "Retry"
                    )
                )
            }
        }
    }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(categoriesList) { category ->
            ImageButton(url = category.imageUrl,
                contentDescription = category.name,
                text = category.name,
                onClick = {
                    onNavigate(category.navigateUrl)
                })
        }
    }
}
