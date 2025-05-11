package com.napzak.market.explore.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.genre.model.Genre
import com.napzak.market.product.model.Product

@Immutable
data class ExploreUiState(
    val loadState: UiState<ExploreProducts> = UiState.Loading,
    val selectedTab: TradeType = TradeType.SELL,
    val filteredGenres: List<Genre> = emptyList(),
    val initGenreItems: List<Genre> = emptyList(),
    val genreSearchResultItems: List<Genre> = emptyList(),
    val isUnopenSelected: Boolean = false,
    val isSoldOutSelected: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)

data class ExploreProducts(
    val productList: List<Product>,
)
