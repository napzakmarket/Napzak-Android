package com.napzak.market.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.napzak.market.chat.navigation.navigateToChatList
import com.napzak.market.explore.navigation.navigateToExplore
import com.napzak.market.home.navigation.Home
import com.napzak.market.home.navigation.navigateToHome
import com.napzak.market.mypage.navigation.Wishlist
import com.napzak.market.mypage.navigation.navigateToMyPage
import com.napzak.market.splash.navigation.Splash

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Splash

    var isRegister: Boolean by mutableStateOf(false)
        private set

    val currentTab: MainTab?
        @Composable get() =
            when {
                isRegister -> MainTab.REGISTER
                currentDestination?.hasRoute(Wishlist::class) == true -> MainTab.MY_PAGE
                else -> MainTab.find { tab -> currentDestination?.hasRoute(tab::class) == true }
            }

    fun navigate(tab: MainTab) {
        if (tab != MainTab.REGISTER && isRegister) dismissRegisterDialog()

        val navOptions = navOptions {
            navController.getBackStackEntry<Home>().destination.route?.let {
                popUpTo(it)
            }
            launchSingleTop = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions = navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore(navOptions = navOptions)
            MainTab.REGISTER -> {
                isRegister = isRegister.not()
            }

            MainTab.CHAT -> navController.navigateToChatList(navOptions = navOptions)
            MainTab.MY_PAGE -> navController.navigateToMyPage(navOptions = navOptions)
        }
    }

    fun navigateUp() {
        if (isRegister) dismissRegisterDialog()
        else navController.navigateUp()
    }

    @Composable
    fun showBottomBar(): Boolean {
        val isMainTabRoute = MainTab.contains {
            currentDestination?.hasRoute(it::class) == true
        }

        val isAdditionalBottomRoute =
            currentDestination?.hasRoute(Wishlist::class) == true

        return isMainTabRoute || isAdditionalBottomRoute
    }

    fun dismissRegisterDialog() {
        isRegister = false
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}