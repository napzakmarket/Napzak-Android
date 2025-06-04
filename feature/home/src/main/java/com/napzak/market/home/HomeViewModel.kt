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
import com.napzak.market.util.common.groupBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val nickname = MutableStateFlow("")
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
            nickname = nickname.value,
            bannerLoadState = bannerLoadState,
            recommendProductLoadState = recommendProductLoadState,
            popularSellLoadState = popularSellLoadState,
            popularBuyLoadState = popularBuyLoadState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(
            nickname = "",
            bannerLoadState = UiState.Loading,
            recommendProductLoadState = UiState.Loading,
            popularSellLoadState = UiState.Loading,
            popularBuyLoadState = UiState.Loading,
        )
    )

    private val _sideEffect = Channel<HomeSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val interestDebounceFlow = MutableSharedFlow<Pair<Long, Boolean>>()

    init {
        handleInterestDebounce()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun handleInterestDebounce() = viewModelScope.launch {
        interestDebounceFlow
            .groupBy { it.first }
            .flatMapMerge { (_, flow) -> flow.debounce(300L) }
            .collect { (productId, isInterest) ->
                interestProductUseCase(productId, isInterest)
                    .onSuccess {
                        getHomeProducts()

                        //이전 상태를 기반으로 현재 좋아요 여부를 판단
                        if (!isInterest) _sideEffect.send(HomeSideEffect.ShowInterestToast)
                    }
                    .onFailure(Timber::e)
            }
    }

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
            .onSuccess {
                nickname.value = it.first
                _recommendProductLoadState.value = UiState.Success(it.second)
            }
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
        interestDebounceFlow.emit(productId to isInterest)
    }
}
