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
import javax.inject.Inject

interface MainNavigator {
    val appNavigator: AppNavigator
    val isRegister: Boolean
    val currentTab: MainTab?
    val currentScreen: ScreenKey?

    fun navigateTab(tab: MainTab)
    fun navigateUp()
    fun setRegistrationTabSelected(value: Boolean)
    fun showBottomBar(): Boolean
}

class MainNavigatorImpl @Inject constructor(
    override val appNavigator: AppNavigator,
) : MainNavigator {
    override var isRegister: Boolean by mutableStateOf(false)
        private set

    override val currentScreen: ScreenKey?
        get() = appNavigator.backStack.lastOrNull()

    override val currentTab: MainTab?
        get() = when {
            isRegister -> MainTab.REGISTER
            currentScreen == WishlistScreenKey -> MainTab.MY_PAGE
            else -> MainTab.find { tab -> currentScreen == tab }
        }

    override fun navigateTab(tab: MainTab) {
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

        if (existingIndex != -1) {
            val numToRemove = backStack.size - 1 - existingIndex
            repeat(numToRemove) { appNavigator.pop() }
        } else {
            appNavigator.navigateTo(key)
        }
    }

    override fun navigateUp() {
        appNavigator.pop()
    }

    override fun setRegistrationTabSelected(value: Boolean) {
        isRegister = value
    }

    override fun showBottomBar(): Boolean {
        return MainTab.contains { currentScreen == it }
    }
}
