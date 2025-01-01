package com.zellfresh.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            contentDescription = if (isDarkTheme) "Light Mode" else "Dark Mode",
            tint = Color.White,
            modifier = modifier
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ThemeTogglePreview() {
    var isDarkTheme by remember { mutableStateOf(true) }
    ThemeToggle(isDarkTheme = isDarkTheme, onClick = {
        isDarkTheme = !isDarkTheme
    })
}