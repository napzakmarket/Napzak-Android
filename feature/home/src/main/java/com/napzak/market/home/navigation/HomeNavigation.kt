package com.napzak.market.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.home.HomeRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToHome(navOptions: NavOptions? = null) = navigate(Home, navOptions)

fun NavGraphBuilder.homeGraph(
    navigateToSearch: () -> Unit,
    navigateToProductDetail: (Long) -> Unit,
    navigateToExploreSell: () -> Unit,
    navigateToExploreBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<Home> {
        HomeRoute(
            onSearchNavigate = navigateToSearch,
            onProductDetailNavigate = navigateToProductDetail,
            onMostInterestedSellNavigate = navigateToExploreSell,
            onMostInterestedBuyNavigate = navigateToExploreBuy,
            modifier = modifier
        )
    }
}

@Serializable
data object Home : MainTabRoute