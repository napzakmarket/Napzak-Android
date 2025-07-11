package com.napzak.market.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigateTo = intent.getStringExtra(NAVIGATE_KEY)
            val chatRoomId = intent.getLongExtra(CHAT_ROOD_ID_KEY, -1)
            val shouldSkipSplash = navigateTo != null // FCM 클릭 시엔 Splash 건너뜀

            NapzakMarketTheme {
                val navigator = rememberMainNavigator(shouldSkipSplash = shouldSkipSplash)

                MainScreen(
                    restartApplication = ::restartApplication,
                    navigateTo = navigateTo,
                    chatRoomId = chatRoomId,
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

    companion object {
        const val NAVIGATE_KEY = "navigateTo"
        const val CHAT_ROOD_ID_KEY = "chatRoomId"
    }
}