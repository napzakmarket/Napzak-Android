package com.napzak.market.mypage.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.mypage.MyPageRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) =
    navigate(MyPageNavigation, navOptions)

fun NavGraphBuilder.mypageGraph(
    navigateToMyMarket: () -> Unit,
    navigateToSales: () -> Unit,
    navigateToPurchase: () -> Unit,
    navigateToRecent: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToHelp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<MyPageNavigation> {
        MyPageRoute(
            onMyMarketClick = navigateToMyMarket,
            onSalesClick = navigateToSales,
            onPurchaseClick = navigateToPurchase,
            onRecentClick = navigateToRecent,
            onFavoriteClick = navigateToFavorite,
            onSettingsClick = navigateToSettings,
            onHelpClick = navigateToHelp,
            modifier = modifier,
        )
    }
}

@Serializable
data object MyPageNavigation : MainTabRoute