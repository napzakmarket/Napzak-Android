package com.napzak.market.config.messaging

import com.google.firebase.messaging.FirebaseMessaging
import com.napzak.market.local.datastore.NotificationDataStore
import com.napzak.market.notification.repository.FirebaseRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val notificationDataStore: NotificationDataStore,
) : FirebaseRepository {
    override suspend fun getPushTokenFromFirebase() {
        runCatching {
            FirebaseMessaging.getInstance().token.await()
        }
            .onSuccess { notificationDataStore.setPushToken(it) }
            .onFailure(Timber::e)
    }

    override suspend fun deletePushTokenFromFirebase() {
        runCatching {
            FirebaseMessaging.getInstance().deleteToken().await()
        }
            .onSuccess { notificationDataStore.clearPushToken() }
            .onFailure(Timber::e)
    }
}
