package com.zellfresh.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zellfresh.ui.components.shimmerLoading


@Composable
fun CartItemSkeleton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = modifier
                .size(80.dp)
                .padding(4.dp)
                .shimmerLoading()
                .background(Color.LightGray)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.5f)
                    .shimmerLoading()
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.8f)
                    .shimmerLoading()
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .height(14.dp)
                    .fillMaxWidth(0.6f)
                    .shimmerLoading()
                    .background(Color.LightGray)
            )
        }

        Box(
            modifier = Modifier
                .height(20.dp)
                .width(60.dp)
                .shimmerLoading()
                .background(Color.LightGray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemSkeletonPreview() {
    CartItemSkeleton()
}
