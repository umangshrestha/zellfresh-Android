package com.zellfresh.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apollographql.apollo.api.Optional
import com.zellfresh.client.ListProductsQuery
import com.zellfresh.ui.components.categories.CategoriesViewModel
import com.zellfresh.ui.components.ProductItem

@Composable
fun ProductsScreen(
    onAddItemToCart: (String) -> Unit,
    modifier: Modifier = Modifier,
    category: String?,
) {
    var productsList by remember {
        mutableStateOf(
            emptyList<ListProductsQuery.Item>()
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (productsList.isEmpty()) {
            Text(text = "No products found", modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(productsList) { product ->
                ProductItem(
                    name = product.name,
                    unit = product.unit,
                    description = product.description,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    availableQuantity = product.availableQuantity,
                    rating = product.rating,
                    productId = product.productId,
                    modifier = modifier,
                    onAddItemToCart = onAddItemToCart
                )
            }
        }
    }


}