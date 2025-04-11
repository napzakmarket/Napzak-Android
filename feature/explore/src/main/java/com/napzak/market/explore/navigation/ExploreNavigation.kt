package com.napzak.market.explore.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.explore.ExploreRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToExplore(
    searchTerm: String,
    navOptions: NavOptions? = null,
) = navigate(Explore(searchTerm), navOptions)

fun NavGraphBuilder.exploreGraph(
    navigateToPrevious: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToGenreDetail: (Long) -> Unit,
    navigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Explore> { backStackEntry ->
        val searchTerm: String = backStackEntry.toRoute()

        ExploreRoute(
            searchTerm = searchTerm,
            onBackButtonClick = navigateToPrevious,
            onSearchNavigate = navigateToSearch,
            onGenreDetailNavigate = navigateToGenreDetail,
            onProductDetailNavigate = navigateToProductDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data class Explore(
    val searchTerm: String? = null,
) : MainTabRoute