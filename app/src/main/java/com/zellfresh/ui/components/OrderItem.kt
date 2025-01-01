package com.zellfresh.ui.components

import androidx.compose.foundation.layout.*
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
import com.zellfresh.ui.components.dto.OrderProduct

@Composable
fun OrderItem(
    price: Double,
    quantity: Int,
    product: OrderProduct = OrderProduct.default
) {
    val (name, unit, imageUrl, description) = product
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(80.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2
            )
            Text(
                text = "Rs. $price / $unit",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "Rs. ${price * quantity}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OrderItemPreview() {
    OrderItem(
        product = OrderProduct(
            name = "Product Name",
            unit = "Unit",
            description = "Product Description",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiXM1f7aFP4rKF-wJZ2juCb-7JcQCspEYUVwLK4JrpBdVtRB-ELAqpUCmkg6znfoG4fh8&usqp=CAU",
        ),
        price = 10.99,
        quantity = 2
    )
}




