package com.napzak.market.explore.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.explore.ExploreRoute
import com.napzak.market.explore.ExploreViewModel
import com.napzak.market.explore.genredetail.GenreDetailRoute
import com.napzak.market.explore.genredetail.GenreDetailViewModel
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ExploreScreenKey
import com.napzak.market.navigation.keys.GenreDetailScreenKey
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SearchScreenKey
import com.napzak.market.navigation.util.assistedEntry
import javax.inject.Inject

class ExploreEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        assistedEntry<ExploreViewModel, ExploreViewModel.Factory, ExploreScreenKey> { _, viewModel ->
            ExploreRoute(
                onSearchNavigate = ::navigateToSearch,
                onGenreDetailNavigate = ::navigateToGenreDetail,
                onProductDetailNavigate = ::navigateToProductDetail,
                viewModel = viewModel,
            )
        }

        assistedEntry<GenreDetailViewModel, GenreDetailViewModel.Factory, GenreDetailScreenKey> { _, viewModel ->
            GenreDetailRoute(
                onBackButtonClick = navigator::pop,
                onHomeNavigate = ::popUpToHome,
                onProductClick = { productId -> navigateToProductDetail(productId, ExploreTracker.SOURCE_GENRE_PAGE) },
                viewModel = viewModel,
            )
        }
    }

    private fun navigateToSearch() {
        navigator.navigateTo(SearchScreenKey)
    }

    private fun navigateToGenreDetail(genreId: Long) {
        navigator.navigateTo(GenreDetailScreenKey(genreId = genreId))
    }

    private fun navigateToProductDetail(productId: Long, source: String? = null) {
        navigator.navigateTo(ProductDetailScreenKey(productId = productId, source = source))
    }

    private fun popUpToHome() {
        if(navigator.backStack.contains(HomeScreenKey)) {
            while(navigator.currentScreen != HomeScreenKey) navigator.pop()
        }
    }
}
