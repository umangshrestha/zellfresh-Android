package com.zellfresh.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ErrorComponent(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Error: $text", modifier = modifier
    )
}