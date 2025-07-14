package com.napzak.market.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val deepLinkState = mutableStateOf<Uri?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepLinkState.value = intent?.data
        enableEdgeToEdge()
        setContent {
//            val deepLinkUri = intent.getStringExtra("deep_link_uri")?.toUri()
//            Log.d("fcm", "onCreate: $deepLinkUri")
            val deepLinkUri = deepLinkState.value
            NapzakMarketTheme {
                val navigator = rememberMainNavigator(shouldSkipSplash = deepLinkUri != null)
                MainScreen(
                    restartApplication = ::restartApplication,
                    deepLinkUri = deepLinkUri,
                    navigator = navigator,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

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
        deepLinkState.value = intent.data
        Log.d("fcm_mainActivity", "intent ${intent}")
    }
}