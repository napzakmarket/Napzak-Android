package com.napzak.market.mypage.wishlist.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeType
import com.napzak.market.product.model.Product

@Immutable
data class WishlistUiState(
    val loadState: UiState<WishListProducts> = UiState.Loading,
    val selectedTab: TradeType = TradeType.SELL,
)

data class WishListProducts(
    val interestProducts: List<Product>,
    val nextCursor: String? = null,
)
