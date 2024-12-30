package com.zellfresh.ui.components.notification

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable

data class NotificationEvent(
    val message: String,
    val actionLabel: String? = null,
    val action: () -> Unit = {},
    val duration: SnackbarDuration = SnackbarDuration.Short
)
