package com.napzak.market.notification.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenRequest(
    @SerialName("deviceToken") val deviceToken: String,
    @SerialName("platform") val platform: String = "ANDROID",
    @SerialName("isEnabled") val isEnabled: Boolean,
    @SerialName("allowMessage") val allowMessage: Boolean,
)
