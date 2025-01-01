package com.zellfresh.ui.components.imagebutton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zellfresh.ui.components.shimmerLoading


@Composable
fun ImageButtonSkeleton(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .shimmerLoading()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .shimmerLoading(800)
                    .height(24.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ImageButtonSkeletonPreview() {
    ImageButtonSkeleton()
}