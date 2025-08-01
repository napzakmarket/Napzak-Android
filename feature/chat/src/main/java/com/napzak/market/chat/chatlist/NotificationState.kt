package com.napzak.market.chat.chatlist

import androidx.compose.runtime.Immutable

@Immutable
data class NotificationState(
    val isNotificationModalOpen: Boolean = false,
    val isAppPermissionGranted: Boolean = false,
)
