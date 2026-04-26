package com.napzak.market.navigation.keys

import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import kotlinx.serialization.Serializable

@Serializable
data class ExploreScreenKey(
    val searchTerm: String = "",
    val tradeType: TradeType = TradeType.SELL,
    val sortType: SortType = SortType.RECENT,
) : MainTabScreenKey

@Serializable
data object SearchScreenKey : ScreenKey

@Serializable
data class GenreDetailScreenKey(
    val genreId: Long,
) : ScreenKey
