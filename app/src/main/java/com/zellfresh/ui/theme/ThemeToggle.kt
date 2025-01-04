package com.zellfresh.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zellfresh.R
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
    ) {
        Image(
            painter = painterResource(
                id = if (isDarkTheme) R.drawable.light else R.drawable.dark
            ),
            contentDescription = if (isDarkTheme) "Light Mode" else "Dark Mode",
            modifier = modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ThemeTogglePreview() {
    var isDarkTheme by remember { mutableStateOf(true) }
    ThemeToggle(
        isDarkTheme = isDarkTheme,
        onClick = { isDarkTheme = !isDarkTheme },
        modifier = Modifier.background(Color.Black)
    )
}