package com.napzak.market.explore.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.explore.model.Product

@Immutable
data class ExploreUiState(
    val loadState: UiState<ExploreProducts> = UiState.Loading,
    val selectedTab: TradeType = TradeType.SELL,
    val filteredGenres: List<Genre> = emptyList<Genre>(),
    val initGenreItems: List<Genre> = emptyList<Genre>(),
    val genreSearchResultItems: List<Genre> = emptyList<Genre>(),
    val isUnopenSelected: Boolean = false,
    val isSoldOutSelected: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)

data class ExploreProducts(
    val productList: List<Product>,
)
