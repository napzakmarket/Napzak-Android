package com.napzak.market.notification.model

data class EntireNotificationSettings(
    val pushToken: String,
    val isEnabled: Boolean,
    val allowMessage: Boolean,
)
