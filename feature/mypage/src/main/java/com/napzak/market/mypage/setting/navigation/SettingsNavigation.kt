package com.napzak.market.mypage.setting.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.mypage.setting.SettingsRoute

const val SETTINGS_ROUTE = "settings"

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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
    }
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}