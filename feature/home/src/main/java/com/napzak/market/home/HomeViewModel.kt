package com.napzak.market.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.home.state.HomeUiState
import com.napzak.market.home.type.HomeProductType
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.PatchNotificationSettingsUseCase
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductRecommendationRepository
import com.napzak.market.repository.BannerRepository
import com.napzak.market.type.HomeBannerType
import com.napzak.market.ui_util.groupBy
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val productRepository: ProductRecommendationRepository,
    private val bannerRepository: BannerRepository,
    private val interestProductUseCase: SetInterestProductUseCase,
    private val notificationRepository: NotificationRepository,
    private val patchNotificationSettingsUseCase: PatchNotificationSettingsUseCase,
) : ViewModel() {
    private val nickname = MutableStateFlow("")
    private val _bannerLoadState =
        MutableStateFlow<UiState<Map<HomeBannerType, List<Banner>>>>(UiState.Loading)
    private val _recommendProductLoadState =
        MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    private val _popularSellLoadState =
        MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    private val _popularBuyLoadState =
        MutableStateFlow<UiState<List<Product>>>(UiState.Loading)


    private val lastSuccessfulLoadedProducts =
        mutableMapOf<HomeProductType, UiState<List<Product>>>()

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
            .flatMapMerge { (_, flow) -> flow.debounce(DEBOUNCE_DELAY) }
            .collect { (productId, isInterested) ->
                interestProductUseCase(productId, isInterested)
                    .onSuccess {
                        fetchRecommendedProducts()
                        fetchPopularSellProducts()
                        fetchPopularBuyProducts()
                    }
                    .onFailure {
                        // TODO: 통신 실패에 대한 처리 (현재: 마지막 성공 상태로 복구)
                        Timber.e(it.message.toString())
                        lastSuccessfulLoadedProducts[HomeProductType.RECOMMEND]?.let { recommendProductState ->
                            _recommendProductLoadState.value = recommendProductState
                        }
                        lastSuccessfulLoadedProducts[HomeProductType.POPULAR_SELL]?.let { popularSellState ->
                            _popularSellLoadState.value = popularSellState
                        }
                        lastSuccessfulLoadedProducts[HomeProductType.POPULAR_BUY]?.let { popularBuyState ->
                            _popularBuyLoadState.value = popularBuyState
                        }
                    }
            }
    }

    fun fetchHomeData() {
        fetchBanners()
        fetchRecommendedProducts()
        fetchPopularSellProducts()
        fetchPopularBuyProducts()
    }

    private fun fetchBanners() = viewModelScope.launch {
        bannerRepository.getHomeBanner()
            .onSuccess { _bannerLoadState.value = UiState.Success(it) }
            .onFailure { _bannerLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private fun fetchRecommendedProducts() = viewModelScope.launch {
        productRepository.getRecommendedProducts()
            .onSuccess {
                nickname.value = it.first
                _recommendProductLoadState.value = UiState.Success(it.second)
                lastSuccessfulLoadedProducts[HomeProductType.RECOMMEND] = UiState.Success(it.second)
            }
            .onFailure { _recommendProductLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private fun fetchPopularSellProducts() = viewModelScope.launch {
        productRepository.getPopularSellProducts()
            .onSuccess {
                _popularSellLoadState.value = UiState.Success(it)
                lastSuccessfulLoadedProducts[HomeProductType.POPULAR_SELL] = UiState.Success(it)
            }
            .onFailure { _popularSellLoadState.value = UiState.Failure(it.message.toString()) }
    }

    private fun fetchPopularBuyProducts() = viewModelScope.launch {
        productRepository.getPopularBuyProducts()
            .onSuccess {
                _popularBuyLoadState.value = UiState.Success(it)
                lastSuccessfulLoadedProducts[HomeProductType.POPULAR_BUY] = UiState.Success(it)
            }
            .onFailure { _popularBuyLoadState.value = UiState.Failure(it.message.toString()) }
    }

    fun setInterest(
        productId: Long,
        isInterested: Boolean,
        productType: HomeProductType = HomeProductType.RECOMMEND
    ) = viewModelScope.launch {
        val flow = when (productType) {
            HomeProductType.RECOMMEND -> _recommendProductLoadState
            HomeProductType.POPULAR_SELL -> _popularSellLoadState
            HomeProductType.POPULAR_BUY -> _popularBuyLoadState
        }

        flow.update { currentState ->
            (currentState as UiState.Success<List<Product>>).copy(
                data = currentState.data.map {
                    if (it.productId == productId) {
                        interestDebounceFlow.emit(productId to isInterested)
                        it.copy(isInterested = !isInterested)
                    } else {
                        it
                    }
                }
            )
        }

        //이전 상태를 기반으로 현재 좋아요 여부를 판단
        when (isInterested) {
            true -> _sideEffect.send(HomeSideEffect.CancelInterestToast)
            false -> _sideEffect.send(HomeSideEffect.ShowInterestToast)
        }
    }

    fun updateNotificationSettings(allowMessage: Boolean) = viewModelScope.launch {
        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) patchNotificationSettingsUseCase.invoke(pushToken, allowMessage)
            .onSuccess { notificationRepository.setNotificationPermission(allowMessage) }
            .onFailure { Timber.e(it) }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
