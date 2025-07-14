package com.napzak.market.config.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.napzak.market.R.drawable.ic_push_notification
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import timber.log.Timber

class NapzakFirebaseMessaging : LifecycleAwareFirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "납작 채팅 제목"
        val body = message.notification?.body ?: "납작 채팅 내용"
        val chatRoomId = message.notification?.body

        if (chatRoomId != null) {
            val uri = Uri.parse("myapp://chat/$chatRoomId")

            Timber.tag("FCM").d("onMessageReceived: uri $uri\ntitle $title\nbody $body")

            val intent = Intent(this, DeepLinkReceiver::class.java).apply {
                action = "com.napzak.OPEN_DEEP_LINK"
                putExtra("deep_link_uri", uri.toString())
            }

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "납작 푸시 알림",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notifyId = System.currentTimeMillis().toInt()
            val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(ic_push_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notifyId, notification)
        } else {
            Timber.tag("Napzak FCM").e("chatRoomId is missing from FCM data payload")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM Token").d(token)
        // TODO: 토큰 저장 API 연결
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Napzak"
    }
}
