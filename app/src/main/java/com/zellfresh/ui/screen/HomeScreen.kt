package com.zellfresh.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.NotificationEvent
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(onNavigate: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "This is Home Page",
            modifier = modifier
        )
        IconButton(
            onClick = onNavigate,
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
        }
        Button(
            onClick = {
                scope.launch {
                    NotificationController.notify(
                        NotificationEvent("APPLE")
                    )
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Notify")
        }
    }
}
