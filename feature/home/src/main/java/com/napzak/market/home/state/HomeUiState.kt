package com.napzak.market.home.state

import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.product.model.Product
import com.napzak.market.type.HomeBannerType

internal data class HomeUiState(
    val nickname: String,
    val bannerLoadState: UiState<Map<HomeBannerType, List<Banner>>>,
    val recommendProductLoadState: UiState<List<Product>>,
    val popularSellLoadState: UiState<List<Product>>,
    val popularBuyLoadState: UiState<List<Product>>,
) {
    val isLoaded: UiState<Unit>
        get() = when {
            bannerLoadState is UiState.Success
                    && recommendProductLoadState is UiState.Success
                    && popularSellLoadState is UiState.Success
                    && popularBuyLoadState is UiState.Success -> UiState.Success(Unit)

            bannerLoadState is UiState.Failure
                    || recommendProductLoadState is UiState.Failure
                    || popularSellLoadState is UiState.Failure
                    || popularBuyLoadState is UiState.Failure -> UiState.Failure("failed to load data")


            bannerLoadState is UiState.Loading
                    || recommendProductLoadState is UiState.Loading
                    || popularSellLoadState is UiState.Loading
                    || popularBuyLoadState is UiState.Loading -> UiState.Loading

            else -> UiState.Empty
        }
}
