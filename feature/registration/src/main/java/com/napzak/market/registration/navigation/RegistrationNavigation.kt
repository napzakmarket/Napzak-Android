package com.napzak.market.registration.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.registration.genre.GenreSearchRoute
import com.napzak.market.registration.purchase.PurchaseRegistrationRoute
import com.napzak.market.registration.sale.SaleRegistrationRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToSaleRegistration(navOptions: NavOptions? = null) =
    navigate(SaleRegistration, navOptions)

fun NavController.navigateToPurchaseRegistration(navOptions: NavOptions? = null) =
    navigate(PurchaseRegistration, navOptions)

fun NavController.navigateToGenreSearch(navOptions: NavOptions? = null) =
    navigate(GenreSearch, navOptions)

fun NavGraphBuilder.registrationGraph(
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    navigateToGenreSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<SaleRegistration> {
        SaleRegistrationRoute(
            navigateToUp = navigateToUp,
            navigateToDetail = navigateToDetail,
            navigateToGenreSearch = navigateToGenreSearch,
            modifier = modifier,
        )
    }

    composable<PurchaseRegistration> {
        PurchaseRegistrationRoute(
            navigateToUp = navigateToUp,
            navigateToDetail = navigateToDetail,
            navigateToGenreSearch = navigateToGenreSearch,
            modifier = modifier,
        )
    }

    composable<GenreSearch> { backStackEntry ->
        GenreSearchRoute(
            navigateToUp = navigateToUp,
            modifier = modifier,
        )
    }
}

@Serializable
data object SaleRegistration : Route

@Serializable
data object PurchaseRegistration : Route

@Serializable
data object GenreSearch : Route
