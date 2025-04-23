package com.napzak.market.explore.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.explore.ExploreRoute
import com.napzak.market.explore.genredetail.GenreDetailRoute
import kotlinx.serialization.Serializable

const val EMPTY_TEXT = ""

fun NavController.navigateToExplore(
    searchTerm: String = EMPTY_TEXT,
    navOptions: NavOptions? = null,
) = navigate(Explore(searchTerm), navOptions)

fun NavController.navigateToGenreDetail(
    genreId: Long,
    navOptions: NavOptions? = null,
) = navigate(GenreDetail(genreId), navOptions)

fun NavGraphBuilder.exploreGraph(
    navigateToUp: () -> Unit,
    navigateToHome: () -> Unit,
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

    composable<GenreDetail> {
        GenreDetailRoute(
            onBackButtonClick = navigateToUp,
            onHomeNavigate = navigateToHome,
            onProductClick = navigateToProductDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data class Explore(
    val searchTerm: String = EMPTY_TEXT,
) : MainTabRoute

@Serializable
data class GenreDetail(
    val genreId: Long,
) : Route