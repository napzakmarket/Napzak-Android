package com.napzak.market.explore.genredetail.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.genre.model.GenreInfo
import com.napzak.market.product.model.Product

@Immutable
data class GenreDetailUiState(
    val loadState: UiState<GenreDetailProducts> = UiState.Loading,
    val genreInfo: GenreInfo = GenreInfo(0, "", "", ""),
    val selectedTab: TradeType = TradeType.SELL,
    val isUnopenSelected: Boolean = false,
    val isSoldOutSelected: Boolean = false,
    val sortOption: SortType = SortType.RECENT,
)

data class GenreDetailProducts(
    val productCount: Int,
    val productList: List<Product>,
)
