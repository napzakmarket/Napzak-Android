package com.napzak.market.splash.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.event.SocketEventBus
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.LoginScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SplashScreenKey
import com.napzak.market.splash.SplashRoute
import javax.inject.Inject

class SplashEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
    private val socketEventBus: SocketEventBus,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<SplashScreenKey> {
            SplashRoute(
                onNavigateToMain = ::navigateToMain,
                onNavigateToLogin = ::navigateToLogin,
            )
        }
    }

    private fun navigateToMain() {
        socketEventBus.connect()
        navigator.navigateTo(HomeScreenKey)
    }

    private fun navigateToLogin() {
        navigator.navigateTo(LoginScreenKey)
    }
}
