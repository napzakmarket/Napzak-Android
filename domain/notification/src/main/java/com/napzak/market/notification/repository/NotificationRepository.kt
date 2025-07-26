package com.napzak.market.notification.repository

import com.napzak.market.notification.model.EntireNotificationSettings
import com.napzak.market.notification.model.NotificationSettings

interface NotificationRepository {
    suspend fun updatePushToken(pushToken: EntireNotificationSettings): Result<Unit>
    suspend fun deletePushToken(pushToken: String): Result<Unit>
    suspend fun getNotificationSettings(pushToken: String): Result<NotificationSettings>
    suspend fun patchNotificationSettings(pushToken: String, allowMessage: Boolean): Result<Unit>
}
