package com.napzak.market.config.messaging

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.napzak.market.R.drawable.ic_push_notification
import com.napzak.market.main.MainActivity
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import timber.log.Timber

class NapzakFirebaseMessaging : LifecycleAwareFirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body
        val messageData = message.data
        val notifyType = messageData["type"] ?: ""
        val chatRoomId = messageData["roomId"]

        val uri = Uri.parse("napzak://$notifyType/$chatRoomId")
        val notifyId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            action = "com.napzak.OPEN_DEEP_LINK"
            putExtra("deep_link_uri", uri.toString())
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
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
        Timber.tag("FCM Token").d(token)
        // TODO: 토큰 저장 API 연결
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NAPZAK"
    }
}
