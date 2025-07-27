package com.napzak.market.notification.repositoryimpl

import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.notification.datasource.NotificationDataSource
import com.napzak.market.notification.mapper.toData
import com.napzak.market.notification.mapper.toDomain
import com.napzak.market.notification.model.EntireNotificationSettings
import com.napzak.market.notification.model.NotificationSettings
import com.napzak.market.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun updatePushToken(pushToken: EntireNotificationSettings): Result<Unit> =
        runCatching {
            notificationDataSource.updatePushToken(pushToken.toData())
        }

    override suspend fun deletePushToken(pushToken: String): Result<Unit> =
        runCatching {
            notificationDataSource.deletePushToken(pushToken)
        }

    override suspend fun getNotificationSettings(pushToken: String): Result<NotificationSettings> =
        runCatching {
            val responseData = notificationDataSource.getNotificationSettings(pushToken).data
            responseData.toDomain()
        }

    override suspend fun patchNotificationSettings(
        pushToken: String,
        allowMessage: Boolean,
    ): Result<Unit> =
        runCatching {
            notificationDataSource.patchNotificationSettings(pushToken, allowMessage.toData())
        }

    override suspend fun getPushToken(): String? = tokenDataStore.getPushToken()

    override suspend fun setNotificationPermission(allowMessage: Boolean) =
        tokenDataStore.setNotificationPermission(allowMessage)

    override suspend fun cleanPushToken() {
        tokenDataStore.clearPushToken()
    }
}
