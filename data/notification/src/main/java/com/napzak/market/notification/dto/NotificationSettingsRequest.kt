package com.napzak.market.notification.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsRequest(
    @SerialName("allowMessage") val allowMessage: Boolean,
)
