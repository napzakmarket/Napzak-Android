package com.napzak.market.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.LocalSystemBarsColor
import com.napzak.market.ui_util.SystemBarsColorController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var systemBarsColorController: SystemBarsColorController

    @Inject
    lateinit var webSocketLifecycleObserver: WebSocketLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(webSocketLifecycleObserver)
        enableEdgeToEdge()
        setContent {
            NapzakMarketTheme {
                SystemBarColorHandler()
                CompositionLocalProvider(LocalSystemBarsColor provides systemBarsColorController) {
                    MainScreen(
                        restartApplication = ::restartApplication,
                        connectSocket = { webSocketLifecycleObserver.updateLoggedInState(true) },
                    )
                }
            }
        }
    }

    @Composable
    private fun SystemBarColorHandler() {
        systemBarsColorController.Apply(activity = this)
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
