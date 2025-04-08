package com.napzak.market.explore.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.explore.ExploreRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToExplore(navOptions: NavOptions? = null) = navigate(Explore, navOptions)

fun NavGraphBuilder.exploreGraph(
    navigateToSearch: () -> Unit,
    navigateToGenreDetail: (Long) -> Unit,
    navigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Explore> {
        ExploreRoute(
            onSearchNavigate = navigateToSearch,
            onGenreDetailNavigate = navigateToGenreDetail,
            onProductDetailNavigate = navigateToProductDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data object Explore : MainTabRoute