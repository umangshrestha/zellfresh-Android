package com.zellfresh.ui.components.cart

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CartItem(
    quantity: Int,
    productId: String,
    name: String,
    unit: String,
    price: Double,
    imageUrl: String,
    availableQuantity: Int,
    limitPerTransaction: Int,
    description: String,
    onAddItemToCart: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedQuantity by remember { mutableIntStateOf(quantity) }
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = modifier
                .size(80.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "$description\nRs. $price / $unit",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (quantity > availableQuantity) {
                Text(
                    text = "Only $availableQuantity available. Please reduce the quantity.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rs. ${price * quantity}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.headlineSmall,
            )
            TextButton(
                onClick = { expanded = true },
            ) {
                Text(
                    "Quantity: $selectedQuantity",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },

                ) {
                for (i in 0..minOf(limitPerTransaction, availableQuantity)) {
                    DropdownMenuItem(
                        onClick = {
                            selectedQuantity = i
                            onAddItemToCart(productId, i)
                        },
                        text = {
                            Text(text = if (i == 0) "0 (remove)" else i.toString())
                        }
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun CartItemPreview() {
    var currentQuantity by remember {
        mutableIntStateOf(1)
    }
    CartItem(price = 12.99,
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiXM1f7aFP4rKF-wJZ2juCb-7JcQCspEYUVwLK4JrpBdVtRB-ELAqpUCmkg6znfoG4fh8&usqp=CAU",
        availableQuantity = 10,
        limitPerTransaction = 10,
        name = "Chicken Breast",
        description = "Delicious chicken breast",
        unit = "kg",
        productId = "1731039366706",
        quantity = currentQuantity,
        onAddItemToCart = { id: String, quantity: Int ->
            Log.d("CartItemPreview", "$id $quantity")
            currentQuantity = quantity
        }

    )
}
