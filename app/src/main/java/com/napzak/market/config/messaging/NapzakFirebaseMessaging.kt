package com.napzak.market.config.messaging

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.napzak.market.R.drawable.ic_push_notification
import com.napzak.market.local.datastore.NotificationDataStore
import com.napzak.market.notification.usecase.UpdatePushTokenUseCase
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NapzakFirebaseMessaging : LifecycleAwareFirebaseMessagingService() {

    @Inject
    lateinit var dataStore: NotificationDataStore

    @Inject
    lateinit var updatePushTokenUseCase: UpdatePushTokenUseCase

    @SuppressLint("LaunchActivityFromNotification")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        ensureNotificationChannel()

        val title = message.notification?.title
        val body = message.notification?.body
        val messageData = message.data
        val notifyType = messageData["type"] ?: ""
        val chatRoomId = messageData["roomId"]

        val uri = "napzak://$notifyType/$chatRoomId"
        val notifyId = System.currentTimeMillis().toInt()

        val intent = Intent(this, NotificationClickReceiver::class.java).apply {
            putExtra("deep_link", uri)
            action = OPEN_DEEPLINK_ACTION
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, notifyId, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(ic_push_notification)
            .setContentTitle(title).setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifyId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TAG).d(token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val appPermission = dataStore.getNotificationPermission() == true
                val systemPermission = isNotificationPermissionGranted() && isNotificationEnabled()
                dataStore.setPushToken(token)
                updatePushTokenUseCase(
                    pushToken = token,
                    isEnabled = systemPermission,
                    allowMessage = appPermission,
                )
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "푸시 토큰 저장 오류")
            }
        }
    }

    private fun ensureNotificationChannel() {
        if (SDK_INT >= O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (SDK_INT >= TIRAMISU) {
            checkSelfPermission(POST_NOTIFICATIONS) == PERMISSION_GRANTED
        } else {
            true // 33 미만은 기본적으로 권한 있음
        }
    }

    private fun isNotificationEnabled(): Boolean {
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    companion object {
        private const val TAG = "FCM - okhttp"
        const val NOTIFICATION_CHANNEL_ID = "NAPZAK"
        const val CHANNEL_NAME = "납작 푸시 알림 채널"
        const val OPEN_DEEPLINK_ACTION = "com.napzak.OPEN_DEEP_LINK"
    }
}
