package com.napzak.market.mypage.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.mypage.mypage.MyPageRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) =
    navigate(MyPage, navOptions)

fun NavGraphBuilder.mypageGraph(
    navigateToUp: () -> Unit,
    navigateToMyMarket: (Long) -> Unit,
    navigateToSales: () -> Unit,
    navigateToPurchase: () -> Unit,
    navigateToRecent: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToSettings: () -> Unit,
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
}

@Serializable
data object MyPage : MainTabRoute