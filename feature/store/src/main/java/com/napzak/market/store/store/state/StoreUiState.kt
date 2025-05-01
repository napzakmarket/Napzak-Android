package com.napzak.market.store.store.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.store.model.Product
import com.napzak.market.store.model.StoreInfo

@Immutable
data class StoreUiState(
    val loadState: UiState<StoreInformation> = UiState.Loading,
    val selectedTab: MarketTab = MarketTab.SELL,
    val filteredGenres: List<Genre> = emptyList<Genre>(),
    val initGenreItems: List<Genre> = emptyList<Genre>(),
    val genreSearchResultItems: List<Genre> = emptyList<Genre>(),
    val isOnSale: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)

data class StoreInformation(
    val storeInfo: StoreInfo,
    val productList: List<Product> = emptyList(),
)
