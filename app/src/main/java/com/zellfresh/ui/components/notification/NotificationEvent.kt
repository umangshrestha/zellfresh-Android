package com.zellfresh.ui.components.notification

import androidx.compose.material3.SnackbarDuration

data class NotificationEvent(
    val message: String,
    val actionLabel: String = "Ok",
    val action: () -> Unit = {},
    val duration: SnackbarDuration = SnackbarDuration.Short
)
