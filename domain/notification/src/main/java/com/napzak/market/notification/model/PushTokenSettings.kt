package com.napzak.market.notification.model

data class PushTokenSettings(
    val deviceToken: String,
    val platform: String = "ANDROID",
    val isEnabled: Boolean,
    val allowMessage: Boolean,
)
