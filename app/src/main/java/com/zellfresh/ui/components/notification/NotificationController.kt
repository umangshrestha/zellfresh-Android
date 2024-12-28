package com.zellfresh.ui.components.notification

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object NotificationController {
    private val _notifications = Channel<NotificationEvent>()
    val notifications = _notifications.receiveAsFlow()

    suspend fun notify(notification: NotificationEvent) {
        _notifications.send(notification)
    }
}