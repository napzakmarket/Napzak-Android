package com.napzak.market.notification.model

data class EntireNotificationSettings(
    val deviceToken: String,
    val isEnabled: Boolean,
    val allowMessage: Boolean,
)
