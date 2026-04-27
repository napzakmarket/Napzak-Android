package com.napzak.market.registration.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.genre.model.Genre
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.GenreSearchScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.PurchaseRegistrationScreenKey
import com.napzak.market.navigation.keys.SaleRegistrationScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.util.assistedEntry
import com.napzak.market.registration.genre.GenreSearchRoute
import com.napzak.market.registration.genre.GenreSearchViewModel
import com.napzak.market.registration.purchase.PurchaseRegistrationRoute
import com.napzak.market.registration.purchase.PurchaseRegistrationViewModel
import com.napzak.market.registration.sale.SaleRegistrationRoute
import com.napzak.market.registration.sale.SaleRegistrationViewModel
import com.napzak.market.registration.type.RegistrationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RegistrationEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    private val pendingGenreSelection = MutableStateFlow<Genre?>(null)

    override fun EntryProviderScope<ScreenKey>.provide() {
        assistedEntry<SaleRegistrationViewModel, SaleRegistrationViewModel.Factory, SaleRegistrationScreenKey> { _, viewModel ->
            val registrationUiState by viewModel.registrationUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                pendingGenreSelection.filterNotNull().collect { genre ->
                    viewModel.updateGenre(genre)
                    pendingGenreSelection.value = null
                }
            }

            SaleRegistrationRoute(
                navigateToUp = navigator::pop,
                navigateToDetail = ::navigateToProductDetail,
                navigateToGenreSearch = {
                    navigateToGenreSearch(
                        from = RegistrationType.SALE,
                        selectedGenreId = registrationUiState.genre?.genreId
                    )
                },
                viewModel = viewModel,
            )
        }

        assistedEntry<PurchaseRegistrationViewModel, PurchaseRegistrationViewModel.Factory, PurchaseRegistrationScreenKey> { _, viewModel ->
            val registrationUiState by viewModel.registrationUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                pendingGenreSelection.filterNotNull().collect { genre ->
                    viewModel.updateGenre(genre)
                    pendingGenreSelection.value = null
                }
            }

            PurchaseRegistrationRoute(
                navigateToUp = navigator::pop,
                navigateToDetail = ::navigateToProductDetail,
                navigateToGenreSearch = {
                    navigateToGenreSearch(
                        from = RegistrationType.PURCHASE,
                        selectedGenreId = registrationUiState.genre?.genreId
                    )
                },
                viewModel = viewModel,
            )
        }

        assistedEntry<GenreSearchViewModel, GenreSearchViewModel.Factory, GenreSearchScreenKey> { _, viewModel ->
            GenreSearchRoute(
                navigateToUp = navigator::pop,
                onGenreSelect = { genre ->
                    pendingGenreSelection.update { genre }
                    navigator.pop()
                },
                viewModel = viewModel,
            )
        }
    }

    private fun navigateToProductDetail(productId: Long) {
        navigator.navigateTo(
            key = ProductDetailScreenKey(productId = productId),
            popUpTo = navigator.currentScreen,
            inclusive = true,
            singleTop = true,
        )
    }

    private fun navigateToGenreSearch(
        from: RegistrationType,
        selectedGenreId: Long? = null,
    ) {
        navigator.navigateTo(
            GenreSearchScreenKey(
                from = from.name,
                selectedGenreId = selectedGenreId
            )
        )
    }
}
