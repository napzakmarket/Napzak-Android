package com.napzak.market.registration.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.genre.GenreSearchRoute
import com.napzak.market.registration.purchase.PurchaseRegistrationRoute
import com.napzak.market.registration.purchase.PurchaseRegistrationViewModel
import com.napzak.market.registration.sale.SaleRegistrationRoute
import com.napzak.market.registration.sale.SaleRegistrationViewModel
import com.napzak.market.registration.type.RegistrationType
import kotlinx.serialization.Serializable

fun NavController.navigateToSaleRegistration(
    navOptions: NavOptions? = null,
    productId: Long? = null,
) = navigate(SaleRegistration(productId), navOptions)

fun NavController.navigateToPurchaseRegistration(
    navOptions: NavOptions? = null,
    productId: Long? = null,
) = navigate(PurchaseRegistration(productId), navOptions)

private fun NavController.navigateToGenreSearch(
    from: RegistrationType,
    navOptions: NavOptions? = null,
) = navigate(GenreSearch(from), navOptions)

@Composable
private fun NavHostController.sharedRegistrationViewModel(
    from: RegistrationType,
    parentEntry: NavBackStackEntry,
): RegistrationViewModel =
    when (from) {
        RegistrationType.SALE -> hiltViewModel<SaleRegistrationViewModel>(parentEntry)
        RegistrationType.PURCHASE -> hiltViewModel<PurchaseRegistrationViewModel>(parentEntry)
    }

fun NavGraphBuilder.registrationGraph(
    navController: NavHostController,
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<SaleRegistration> {
        SaleRegistrationRoute(
            navigateToUp = navigateToUp,
            navigateToDetail = navigateToDetail,
            navigateToGenreSearch = {
                navController.navigateToGenreSearch(RegistrationType.SALE)
            },
            modifier = modifier.systemBarsPadding(),
        )
    }

    composable<PurchaseRegistration> {
        PurchaseRegistrationRoute(
            navigateToUp = navigateToUp,
            navigateToDetail = navigateToDetail,
            navigateToGenreSearch = {
                navController.navigateToGenreSearch(RegistrationType.PURCHASE)
            },
            modifier = modifier.systemBarsPadding(),
        )
    }

    composable<GenreSearch> {
        val from = it.toRoute<GenreSearch>().from
        val parentEntry = when (from) {
            RegistrationType.SALE -> navController.getBackStackEntry<SaleRegistration>()
            RegistrationType.PURCHASE -> navController.getBackStackEntry<PurchaseRegistration>()
        }
        val parentViewModel = navController.sharedRegistrationViewModel(from, parentEntry)
        val registrationUiState by parentViewModel.registrationUiState.collectAsStateWithLifecycle()

        GenreSearchRoute(
            navigateToUp = navigateToUp,
            onGenreSelect = { genre ->
                parentViewModel.updateGenre(genre)
                navigateToUp()
            },
            selectedGenreId = registrationUiState.genre?.genreId,
            modifier = modifier.systemBarsPadding(),
        )
    }
}

@Serializable
data class SaleRegistration(
    val productId: Long? = null,
) : Route

@Serializable
data class PurchaseRegistration(
    val productId: Long? = null,
) : Route

@Serializable
data class GenreSearch(
    val from: RegistrationType,
) : Route
