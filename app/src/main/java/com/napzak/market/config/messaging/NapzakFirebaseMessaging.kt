package com.napzak.market.config.messaging

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.napzak.market.R.drawable.ic_app
import com.napzak.market.main.MainActivity
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import timber.log.Timber

class NapzakFirebaseMessaging : LifecycleAwareFirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isEmpty()) return

        val receivedData = message.data
        val notificationId = receivedData["id"] ?: ""
        val title = receivedData["title"] ?: ""
        val body = receivedData["content"] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigateTo", "chat")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notifyId = System.currentTimeMillis().toInt()
        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(ic_app)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM Token").d(token)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Napzak"
    }
}

