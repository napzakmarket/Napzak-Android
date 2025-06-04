package com.napzak.market.mypage.setting.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.mypage.setting.SettingsRoute
import com.napzak.market.ui_util.openUrl
import kotlinx.serialization.Serializable

@Serializable
data object Settings : Route

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(Settings, navOptions)
}

fun NavGraphBuilder.settingsGraph(
    navigateToBack: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    composable<Settings> {
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