package com.napzak.market.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductRecommendationRepository
import com.napzak.market.repository.BannerRepository
import com.napzak.market.type.HomeBannerType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val productRepository: ProductRecommendationRepository,
    private val bannerRepository: BannerRepository,
) : ViewModel() {
    private val _bannerLoadState =
        MutableStateFlow<UiState<Map<HomeBannerType, List<Banner>>>>(UiState.Loading)
    private val _recommendProductLoadState =
        MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    private val _popularSellLoadState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    private val _popularBuyLoadState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)

    val homeUiState: StateFlow<HomeUiState> = combine(
        _bannerLoadState,
        _recommendProductLoadState,
        _popularSellLoadState,
        _popularBuyLoadState,
    ) { bannerLoadState, recommendProductLoadState, popularSellLoadState, popularBuyLoadState ->
        HomeUiState(
            bannerLoadState = bannerLoadState,
            recommendProductLoadState = recommendProductLoadState,
            popularSellLoadState = popularSellLoadState,
            popularBuyLoadState = popularBuyLoadState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(
            bannerLoadState = UiState.Loading,
            recommendProductLoadState = UiState.Loading,
            popularSellLoadState = UiState.Loading,
            popularBuyLoadState = UiState.Loading,
        )
    )

    fun getBanners() = viewModelScope.launch {
        bannerRepository.getHomeBanner()
            .onSuccess { _bannerLoadState.value = UiState.Success(it) }
            .onFailure { _bannerLoadState.value = UiState.Failure(it.message.toString()) }
    }

    fun getRecommendedProducts() = viewModelScope.launch {
        productRepository.getRecommendedProducts()
            .onSuccess { _recommendProductLoadState.value = UiState.Success(it.second) }
            .onFailure { _recommendProductLoadState.value = UiState.Failure(it.message.toString()) }
    }

    fun getPopularSellProducts() = viewModelScope.launch {
        productRepository.getPopularSellProducts()
            .onSuccess { _popularSellLoadState.value = UiState.Success(it) }
            .onFailure { _popularSellLoadState.value = UiState.Failure(it.message.toString()) }
    }

    fun getPopularBuyProducts() = viewModelScope.launch {
        productRepository.getPopularBuyProducts()
            .onSuccess { _popularBuyLoadState.value = UiState.Success(it) }
            .onFailure { _popularBuyLoadState.value = UiState.Failure(it.message.toString()) }
    }
}

internal data class HomeUiState(
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