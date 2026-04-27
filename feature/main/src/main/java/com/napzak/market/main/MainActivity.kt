package com.napzak.market.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.napzak.market.designsystem.component.toast.NapzakToast
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.main.navigation.MainNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.ui_util.disableContrastEnforcement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var webSocketLifecycleObserver: WebSocketLifecycleObserver

    @Inject
    lateinit var entryBuilders: @JvmSuppressWildcards Set<EntryProviderBuilder>

    @Inject
    lateinit var navigator: MainNavigator

    lateinit var napzakToast: NapzakToast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(webSocketLifecycleObserver)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        window.disableContrastEnforcement()

        napzakToast = NapzakToast(this, this)
        setContent {
            NapzakMarketTheme {
                MainScreen(
                    navigator = navigator,
                    entryBuilders = entryBuilders,
                    napzakToast = napzakToast,
                )
            }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        lifecycleScope.launch {
            navigator.appNavigator.handleIntent(intent)
        }
    }
}
