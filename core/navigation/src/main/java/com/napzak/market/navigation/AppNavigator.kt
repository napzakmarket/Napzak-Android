package com.napzak.market.navigation

import android.content.Intent
import androidx.navigation3.runtime.NavBackStack
import com.napzak.market.navigation.keys.ScreenKey

interface AppNavigator {
    val backStack: NavBackStack<ScreenKey>
    val currentScreen: ScreenKey?

    fun navigateTo(
        key: ScreenKey,
        popUpTo: ScreenKey? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = true,
    )

    fun pop()
    suspend fun handleIntent(intent: Intent)
    suspend fun handleDeepLinkEvent()
}
