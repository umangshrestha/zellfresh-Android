package com.zellfresh.ui.components.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zellfresh.client.ListCartsQuery.Item as Cart
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.ErrorComponent

@Composable
fun CartScreen(
    cartList: Result<List<Cart>>,
    onAddItemToCart: (String, Int) -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Categories",
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Center,
            style = typography.headlineLarge,
        )
        LazyColumn(
            modifier = modifier
                .fillMaxHeight()
                .padding(top = 36.dp)
        ) {
            when (cartList) {
                is Result.Failure -> item {
                    ErrorComponent(
                        text = cartList.exception.message ?: "Error fetching data",
                        modifier = modifier
                    )
                }

                is Result.Loading -> {
                    repeat(4) {
                        item {
                            CartItemSkeleton()
                        }
                    }
                }

                is Result.Success -> items(cartList.data) { cart ->
                    CartItem(
                        quantity = cart.quantity,
                        productId = cart.productId,
                        name = cart.product?.name.let { "NA" },
                        unit = cart.product?.unit.let { "kg" },
                        price = cart.product?.price.let { 0.0 },
                        imageUrl = cart.product?.imageUrl.let { "" },
                        availableQuantity = cart.product?.availableQuantity.let { 0 },
                        limitPerTransaction = cart.product?.limitPerTransaction.let { 0 },
                        description = cart.product?.description.let { "" },
                        onAddItemToCart = onAddItemToCart
                    )

                }
            }
        }
        FilledTonalButton(onClick = onCheckout) {
            Text("Checkout", modifier = modifier)
        }
    }

}