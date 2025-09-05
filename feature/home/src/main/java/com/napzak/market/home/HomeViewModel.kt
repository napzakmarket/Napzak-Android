package com.napzak.market.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.home.state.HomeUiState
import com.napzak.market.home.type.HomeProductType
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.mixpanel.MixpanelConstants.BANNER_ID
import com.napzak.market.mixpanel.MixpanelConstants.BANNER_INDEX
import com.napzak.market.mixpanel.MixpanelConstants.BANNER_MAIN
import com.napzak.market.mixpanel.MixpanelConstants.BANNER_MINI
import com.napzak.market.mixpanel.MixpanelConstants.BANNER_TYPE
import com.napzak.market.mixpanel.MixpanelConstants.CLICKED_BANNER
import com.napzak.market.mixpanel.MixpanelConstants.CLICKED_CUSTOM_GENRE
import com.napzak.market.mixpanel.MixpanelConstants.FOR_SALE
import com.napzak.market.mixpanel.MixpanelConstants.ITEM_INDEX
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_POPULAR_SALE
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_POPULAR_WANTED
import com.napzak.market.mixpanel.trackEvent
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.UpdatePushTokenUseCase
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductRecommendationRepository
import com.napzak.market.repository.BannerRepository
import com.napzak.market.store.repository.SettingRepository
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
    private val settingRepository: SettingRepository,
    private val interestProductUseCase: SetInterestProductUseCase,
    private val notificationRepository: NotificationRepository,
    private val updatePushTokenUseCase: UpdatePushTokenUseCase,
    private val mixpanel: MixpanelAPI?,
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
    private val _externalLinks =
        MutableStateFlow<Map<String, String>>(emptyMap())


    private val lastSuccessfulLoadedProducts =
        mutableMapOf<HomeProductType, UiState<List<Product>>>()

    val homeUiState: StateFlow<HomeUiState> = combine(
        _bannerLoadState,
        _recommendProductLoadState,
        _popularSellLoadState,
        _popularBuyLoadState,
        _externalLinks,
    ) { bannerLoadState, recommendProductLoadState, popularSellLoadState, popularBuyLoadState, links ->
        HomeUiState(
            nickname = nickname.value,
            bannerLoadState = bannerLoadState,
            recommendProductLoadState = recommendProductLoadState,
            popularSellLoadState = popularSellLoadState,
            popularBuyLoadState = popularBuyLoadState,
            termsLink = links[KEY_TERMS] ?: "",
            privacyPolicyLink = links[KEY_PRIVACY_POLICY] ?: "",
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
            termsLink = "",
            privacyPolicyLink = "",
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
        getExternalLinks()
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
        productType: HomeProductType = HomeProductType.RECOMMEND,
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

    private fun getExternalLinks() = viewModelScope.launch {
        settingRepository.fetchSettingInfo()
            .onSuccess { settingInfo ->
                _externalLinks.update {
                    mapOf(
                        KEY_TERMS to settingInfo.termsLink,
                        KEY_PRIVACY_POLICY to settingInfo.privacyPolicyLink,
                    )
                }
            }
    }

    fun setNotificationSettings(isEnabled: Boolean) =
        viewModelScope.launch {
            val pushToken = notificationRepository.getPushToken()
            val allowMessage = notificationRepository.getNotificationPermission()
            if (pushToken != null) {
                updateNotificationSettings(pushToken, isEnabled, allowMessage)
            } else Timber.tag(TAG).d("Home-updateNotificationSettings() : pushToken == null")
        }

    private suspend fun updateNotificationSettings(
        pushToken: String,
        isEnabled: Boolean,
        allowMessage: Boolean?,
    ) {
        val allowMessageValue = if (allowMessage == null) isEnabled else allowMessage
        updatePushTokenUseCase(
            pushToken = pushToken,
            isEnabled = isEnabled,
            allowMessage = allowMessageValue,
        )
    }

    suspend fun getNotificationPermissionRequested(): Boolean =
        notificationRepository.getNotificationPermissionRequested() == true

    fun updateNotificationPermissionRequested() = viewModelScope.launch {
        notificationRepository.updateNotificationPermissionRequested()
    }

    internal fun trackClickedBanner(bannerId: Long, bannerType: HomeBannerType, bannerIndex: Int) {
        val props = mapOf(
            BANNER_ID to bannerId,
            BANNER_TYPE to if (bannerType == HomeBannerType.TOP) BANNER_MAIN else BANNER_MINI,
            BANNER_INDEX to bannerIndex,
        )

        mixpanel?.trackEvent(CLICKED_BANNER, props)
    }

    internal fun trackClickedRecommendProduct(index: Int) {
        val props = mapOf(
            ITEM_INDEX to index,
        )
        mixpanel?.trackEvent(CLICKED_CUSTOM_GENRE, props)
    }

    internal fun trackClickedPopularProduct(tradeType: String) {
        val eventName = if (tradeType == FOR_SALE) VIEWED_POPULAR_SALE else VIEWED_POPULAR_WANTED
        mixpanel?.track(eventName)
    }

    companion object {
        private const val TAG = "FCM_TOKEN"
        private const val DEBOUNCE_DELAY = 300L
        private const val KEY_TERMS = "terms"
        private const val KEY_PRIVACY_POLICY = "privacy_policy"
    }
}
