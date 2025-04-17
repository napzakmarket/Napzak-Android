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
import com.napzak.market.home.navigation.Home
import com.napzak.market.home.navigation.navigateToHome
import com.napzak.market.explore.navigation.navigateToExplore

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Home

    var isRegister: Boolean by mutableStateOf(false)
        private set

    val currentTab: MainTab?
        @Composable get() =
            if (isRegister) MainTab.REGISTER
            else MainTab.find { tab -> currentDestination?.hasRoute(tab::class) == true }

    fun navigate(tab: MainTab) {
        if (tab != MainTab.REGISTER && isRegister) dismissRegisterDialog()

        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore()
            MainTab.REGISTER -> {
                isRegister = isRegister.not()
            }
            MainTab.CHAT -> {} //TODO: 체팅 추가 예정
            MainTab.MY_PAGE -> {} //TODO: 마이페이지 추가 예정
        }
    }

    fun navigateUp() {
        if (isRegister) dismissRegisterDialog()
        else navController.navigateUp()
    }

    @Composable
    fun showBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
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