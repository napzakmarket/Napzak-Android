package com.napzak.market.notification.datasource

import com.napzak.market.notification.dto.NotificationSettingsRequest
import com.napzak.market.notification.mapper.toData
import com.napzak.market.notification.model.EntireNotificationSettings
import com.napzak.market.notification.service.NotificationService
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val notificationService: NotificationService,
) {
    suspend fun updatePushToken(request: EntireNotificationSettings) =
        notificationService.updatePushToken(request = request.toData())

    suspend fun deletePushToken(request: String) =
        notificationService.deletePushToken(deviceToken = request)

    suspend fun getNotificationSettings(request: String) =
        notificationService.getNotificationSettings(deviceToken = request)

    suspend fun patchNotificationSettings(
        deviceToken: String,
        request: Boolean,
    ) =
        notificationService.patchNotificationSettings(
            deviceToken = deviceToken,
            request = NotificationSettingsRequest(request)
        )
}
