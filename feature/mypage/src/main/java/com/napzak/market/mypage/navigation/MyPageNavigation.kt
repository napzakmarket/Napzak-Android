package com.napzak.market.mypage.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.mypage.mypage.MyPageRoute
import com.napzak.market.mypage.setting.SettingsRoute
import com.napzak.market.util.common.openUrl
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) =
    navigate(MyPage, navOptions)

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(Settings, navOptions)
}

fun NavGraphBuilder.mypageGraph(
    restartApplication: () -> Unit,
    navigateToUp: () -> Unit,
    navigateToMyMarket: (Long) -> Unit,
    navigateToSales: () -> Unit,
    navigateToPurchase: () -> Unit,
    navigateToRecent: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<MyPage> {
        BackHandler {
            navigateToUp()
        }

        MyPageRoute(
            onMyMarketClick = navigateToMyMarket,
            onSalesClick = navigateToSales,
            onPurchaseClick = navigateToPurchase,
            onRecentClick = navigateToRecent,
            onFavoriteClick = navigateToFavorite,
            onSettingsClick = navigateToSettings,
            modifier = modifier,
        )
    }

    composable<Settings> {
        val context = LocalContext.current

        SettingsRoute(
            onBackClick = navigateToUp,
            onLogoutConfirm = restartApplication,
            onWithdrawClick = navigateToSignOut,
            openWebLink = { url ->
                context.openUrl(url)
            }
        )
    }
}

@Serializable
data object MyPage : MainTabRoute

@Serializable
data object Settings : Route