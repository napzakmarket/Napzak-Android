package com.napzak.market.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.disableContrastEnforcement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var webSocketLifecycleObserver: WebSocketLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(webSocketLifecycleObserver)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        window.disableContrastEnforcement()

        setContent {
            NapzakMarketTheme {
                MainScreen(
                    restartApplication = ::restartApplication,
                    connectSocket = { webSocketLifecycleObserver.updateLoggedInState(true) },
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

        CoroutineScope(Dispatchers.Main).launch {
            if (notifyType == NOTIFY_CHAT && chatRoomId != null)
                ChatDeepLinkEventBus.send(ChatDeepLinkEvent.ChatRoom(chatRoomId))
        }
    }

    companion object {
        const val NOTIFY_CHAT = "chat"
    }
}
