package com.zellfresh.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zellfresh.client.ListCartsQuery
import com.zellfresh.client.ListCartsQuery.Item as Cart
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.ErrorComponent

@Composable
fun CartScreen(
    cartList: Result<List<Cart>>,
    checkoutDetails: ListCartsQuery.CheckoutDetails,
    onAddItemToCart: (String, Int) -> Unit,
    onCheckout: () -> Unit,
    onClearCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Categories",
            modifier = modifier
                .fillMaxWidth(),
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

                is Result.Success -> {
                    item {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.Right
                        ) {
                            TextButton(onClick = onClearCart, modifier = modifier) {
                                Text("Clear Cart", modifier = modifier)
                            }
                        }
                    }

                    item {
                        if (cartList.data.isEmpty()) {
                            Text(
                                text="Your cart is empty"
                            )
                        }
                    }
                    items(cartList.data) { cart ->
                        CartItem(
                            quantity = cart.quantity,
                            productId = cart.productId,
                            name = cart.product?.name ?: "NA",
                            unit = cart.product?.unit ?: "kg",
                            price = cart.product?.price ?: 0.0,
                            imageUrl = cart.product?.imageUrl ?: "",
                            availableQuantity = cart.product?.availableQuantity ?: 0,
                            limitPerTransaction = cart.product?.limitPerTransaction ?: 0,
                            description = cart.product?.description ?: "",
                            onAddItemToCart = onAddItemToCart
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Subtotal:",
                                fontWeight = FontWeight.Bold,
                                modifier = modifier.weight(1f)
                            )
                            Text(
                                "Rs.${checkoutDetails.subTotal}",
                                modifier = modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = modifier.height(8.dp))

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Delivery Charge:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Rs. ${checkoutDetails.deliveryPrice}",
                                modifier = modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Tax (${checkoutDetails.taxPercentage}%):",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                            Text(
                                "Rs.${checkoutDetails.tax}",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Rs.${checkoutDetails.totalPrice}",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                    item {
                        FilledTonalButton(
                            onClick = onCheckout,
                            modifier = modifier.fillParentMaxWidth(),
                            enabled = checkoutDetails.enableCheckout
                        ) {
                            Text("Checkout", modifier = modifier)
                        }
                    }
                }
            }
        }
    }
}

