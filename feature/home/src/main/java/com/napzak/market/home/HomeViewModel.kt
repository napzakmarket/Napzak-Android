package com.napzak.market.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.home.state.HomeUiState
import com.napzak.market.interest.usecase.SetInterestProductUseCase
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val productRepository: ProductRecommendationRepository,
    private val bannerRepository: BannerRepository,
    private val interestProductUseCase: SetInterestProductUseCase,
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

    suspend fun getHomeProducts() {
        getRecommendedProducts()
        getPopularSellProducts()
        getPopularBuyProducts()
    }

    suspend fun getBanners() {
        bannerRepository.getHomeBanner()
            .onSuccess { _bannerLoadState.value = UiState.Success(it) }
            .onFailure { _bannerLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private suspend fun getRecommendedProducts() {
        productRepository.getRecommendedProducts()
            .onSuccess { _recommendProductLoadState.value = UiState.Success(it.second) }
            .onFailure { _recommendProductLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private suspend fun getPopularSellProducts() {
        productRepository.getPopularSellProducts()
            .onSuccess { _popularSellLoadState.value = UiState.Success(it) }
            .onFailure { _popularSellLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private suspend fun getPopularBuyProducts() {
        productRepository.getPopularBuyProducts()
            .onSuccess { _popularBuyLoadState.value = UiState.Success(it) }
            .onFailure { _popularBuyLoadState.value = UiState.Failure(it.message.toString()) }
    }

    fun setInterest(productId: Long, isInterest: Boolean) = viewModelScope.launch {
        interestProductUseCase(productId, isInterest)
            .onSuccess { getHomeProducts() }
            .onFailure(Timber::e)
    }
}
