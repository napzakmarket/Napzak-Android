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

const val EMPTY_TEXT = ""

fun NavController.navigateToExplore(
    searchTerm: String = EMPTY_TEXT,
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
        val explore: Explore = backStackEntry.toRoute()

        ExploreRoute(
            searchTerm = explore.searchTerm,
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
    val searchTerm: String = EMPTY_TEXT,
) : MainTabRoute