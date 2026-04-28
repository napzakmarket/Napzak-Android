package com.napzak.market.main.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.keys.ChatListScreenKey
import com.napzak.market.navigation.keys.ExploreScreenKey
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.MainTabScreenKey
import com.napzak.market.navigation.keys.MyPageScreenKey
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
            MainTab.HOME -> {
                navigateToWithPopUpToHome(HomeScreenKey)
            }

            MainTab.EXPLORE -> {
                navigateToWithPopUpToHome(ExploreScreenKey())
            }

            MainTab.REGISTER -> {
                setRegistrationTabSelected(!isRegister)
            }

            MainTab.CHAT -> {
                navigateToWithPopUpToHome(ChatListScreenKey)
            }

            MainTab.MY_PAGE -> {
                navigateToWithPopUpToHome(MyPageScreenKey)
            }
        }
    }

    private fun navigateToWithPopUpToHome(key: MainTabScreenKey) {
        val backStack = appNavigator.backStack
        val existingIndex = appNavigator.backStack.indexOfLast { it == key }

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
        return MainTab.contains { currentScreen == it }
    }
}
