package com.zellfresh.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zellfresh.client.ListProductsQuery.Rating


@Composable
fun ProductItem(
    name: String,
    unit: String,
    description: String?,
    modifier: Modifier = Modifier,
    price: Double,
    imageUrl: String,
    availableQuantity: Int,
    rating: Rating = Rating(
        rating = 0.0, count = 0
    ),
    onAddItemToCart: (String) -> Unit,
    productId: String
) {
    val isProductAvailable = availableQuantity > 0
    val badgeText = when {
        availableQuantity <= 0 -> "Out of Stock"
        availableQuantity < 10 -> "Limited Stock"
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (badgeText != null) {
                Text(
                    text = badgeText,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = modifier.fillMaxWidth()
            )

            Text(
                text = description ?: "",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                modifier = modifier.fillMaxWidth()
            )

            Text(
                text = "Rs. $price",
                fontSize = 16.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = unit, fontSize = 12.sp, color = Color.Gray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${rating.rating}", fontSize = 14.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${rating.count})", fontSize = 12.sp, color = Color.Gray
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            Button(
                onClick = { onAddItemToCart(productId) },
                enabled = isProductAvailable,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Add to Cart", color = Color.White, textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    ProductItem(
        name = "Product Name",
        unit = "Unit",
        description = "Product Description",
        price = 10.99,
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiXM1f7aFP4rKF-wJZ2juCb-7JcQCspEYUVwLK4JrpBdVtRB-ELAqpUCmkg6znfoG4fh8&usqp=CAU",
        availableQuantity = 10,
        rating = Rating(4.5, 100),
        onAddItemToCart = {},
        productId = "123"
    )
}





