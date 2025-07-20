package com.napzak.market.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val deepLinkUriState = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "납작 푸시 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        setContent {
            val deepLinkUri = intent.getStringExtra("deep_link_uri")?.toUri()
            deepLinkUriState.value = deepLinkUri

            NapzakMarketTheme {
                val navigator =
                    rememberMainNavigator(shouldSkipSplash = deepLinkUriState.value != null)
                MainScreen(
                    restartApplication = ::restartApplication,
                    deepLinkUri = deepLinkUriState.value,
                    navigator = navigator,
                )
            }
        }
    }

    private fun restartApplication() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val notifyType = intent.getStringExtra("type")
        val chatRoomId = intent.getStringExtra("roomId")
        deepLinkUriState.value = Uri.parse("napzak://$notifyType/$chatRoomId")
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NAPZAK"
    }
}
