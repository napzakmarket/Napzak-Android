package com.napzak.market.chat.chatlist

import androidx.compose.runtime.Immutable

@Immutable
data class NotificationState(
    val isNotificationModalOpen: Boolean,
    val isAppPermissionGranted: Boolean,
) {
    companion object {
        val Empty = NotificationState(
            isNotificationModalOpen = false,
            isAppPermissionGranted = false,
        )
    }
}
