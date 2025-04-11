package com.napzak.market.search.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.search.SearchRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToSearch(navOptions: NavOptions? = null) = navigate(Search, navOptions)

fun NavGraphBuilder.searchGraph(
    navigateToPrevious: () -> Unit,
    navigateToSearchResult: (String) -> Unit,
    navigateToGenreDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Search> {
        SearchRoute(
            onBackButtonClick = navigateToPrevious,
            onSearchResultNavigate = navigateToSearchResult,
            onGenreDetailNavigate = navigateToGenreDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data object Search : Route