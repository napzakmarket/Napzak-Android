package com.napzak.market.main.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.MainTabScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.WishlistScreenKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MainNavigator @Inject constructor(
    val appNavigator: AppNavigator,
) {
    var isRegister: Boolean by mutableStateOf(false)
        private set

    val currentScreen: ScreenKey?
        get() = appNavigator.backStack.lastOrNull()

    val currentTab: MainTab?
        get() = when {
            isRegister -> MainTab.REGISTER
            currentScreen == WishlistScreenKey -> MainTab.MY_PAGE
            else -> MainTab.find { tab -> currentScreen == tab }
        }

    fun navigateTab(tab: MainTab) {
        if (tab != MainTab.REGISTER && isRegister) setRegistrationTabSelected(false)

        when (tab) {
            MainTab.REGISTER -> setRegistrationTabSelected(!isRegister)
            else -> navigateToWithPopUpToHome(tab.route)
        }
    }

    private fun navigateToWithPopUpToHome(key: MainTabScreenKey) {
        appNavigator.navigateTo(
            key = key,
            popUpTo = HomeScreenKey,
        )
    }

    fun navigateUp() {
        appNavigator.pop()
    }

    fun setRegistrationTabSelected(value: Boolean) {
        isRegister = value
    }

    fun showBottomBar(): Boolean {
        val isMainTab = MainTab.contains { currentScreen == it }
        val isWishlistTab = currentScreen == WishlistScreenKey
        return isMainTab || isWishlistTab
    }
}
