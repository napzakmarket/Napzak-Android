package com.napzak.market.mypage.setting.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.mypage.setting.SettingsRoute
import com.napzak.market.util.common.openUrl

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsGraph(
    navigateToBack: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    composable(route = SETTINGS_ROUTE) {
        val context = LocalContext.current

        SettingsRoute(
            onBackClick = navigateToBack,
            onLogoutConfirm = onLogoutConfirm,
            onWithdrawClick = onWithdrawClick,
            openWebLink = { url ->
                context.openUrl(url)
            }
        )
    }
}