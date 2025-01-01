package com.zellfresh.ui.components.imagebutton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ImageButton(
    url: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RectangleShape)
            )
            Text(
                text = text,
                fontSize = 44.sp,
                color = Color.White,
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ImageButtonPreview() {
    ImageButton(
        url = "https://assets.epicurious.com/photos/62f16ed5fe4be95d5a460eed/1:1/w_1920,c_limit/RoastChicken_RECIPE_080420_37993.jpg",
        contentDescription = "Google Description",
        text = "Hello Button",
        onClick = {}
    )
}