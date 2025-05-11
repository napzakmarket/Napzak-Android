package com.napzak.market.store.store.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.genre.model.Genre

@Immutable
data class StoreOptionState(
    val selectedTab: MarketTab = MarketTab.SELL,
    val filteredGenres: List<Genre> = emptyList<Genre>(),
    val initGenreItems: List<Genre> = emptyList<Genre>(),
    val genreSearchResultItems: List<Genre> = emptyList<Genre>(),
    val isOnSale: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)
