package com.napzak.market.notification.mapper

import com.napzak.market.notification.dto.UpdatePushTokenRequest
import com.napzak.market.notification.model.EntireNotificationSettings

fun EntireNotificationSettings.toData(): UpdatePushTokenRequest =
    UpdatePushTokenRequest(
        deviceToken = deviceToken,
        isEnabled = isEnabled,
        allowMessage = allowMessage,
    )
