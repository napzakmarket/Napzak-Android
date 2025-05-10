package com.napzak.market.store.store.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.genre.model.Genre
import com.napzak.market.product.model.Product
import com.napzak.market.store.model.StoreDetail

@Immutable
data class StoreUiState(
    val storeDetailState: UiState<StoreDetail>,
    val storeProductsState: UiState<List<Product>>,
) {
    val isLoaded: UiState<Unit>
        get() = when {
            storeDetailState is UiState.Loading || storeProductsState is UiState.Loading
                -> UiState.Loading

            storeDetailState is UiState.Success && storeProductsState is UiState.Success
                -> UiState.Success(Unit)

            storeDetailState is UiState.Failure || storeProductsState is UiState.Failure
                -> UiState.Failure("failed to load")

            else -> UiState.Empty
        }
}

@Immutable
data class StoreOptionState(
    val selectedTab: MarketTab = MarketTab.SELL,
    val filteredGenres: List<Genre> = emptyList<Genre>(),
    val initGenreItems: List<Genre> = emptyList<Genre>(),
    val genreSearchResultItems: List<Genre> = emptyList<Genre>(),
    val isOnSale: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)
