package com.napzak.market.notification.mapper

import com.napzak.market.notification.dto.NotificationSettingsRequest
import com.napzak.market.notification.dto.NotificationSettingsResponse
import com.napzak.market.notification.dto.UpdatePushTokenRequest
import com.napzak.market.notification.model.EntireNotificationSettings
import com.napzak.market.notification.model.NotificationSettings

fun EntireNotificationSettings.toData(): UpdatePushTokenRequest =
    UpdatePushTokenRequest(
        deviceToken = deviceToken,
        platform = "ANDROID",
        isEnabled = isEnabled,
        allowMessage = allowMessage,
    )

fun Boolean.toData(): NotificationSettingsRequest =
    NotificationSettingsRequest(allowMessage = this)

fun NotificationSettingsResponse.toDomain(): NotificationSettings =
    NotificationSettings(allowMessage = allowMessage)
