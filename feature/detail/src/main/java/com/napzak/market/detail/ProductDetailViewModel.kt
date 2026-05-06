package com.napzak.market.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.detail.ProductDetailSideEffect.CancelToast
import com.napzak.market.detail.ProductDetailSideEffect.NavigateUp
import com.napzak.market.detail.ProductDetailSideEffect.ShowToast
import com.napzak.market.detail.type.ProductDetailToastType
import com.napzak.market.interest.usecase.SetInterestUseCase
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.mixpanel.ReportTracker
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.repository.ProductDetailRepository
import com.napzak.market.store.usecase.GetPhoneVerificationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productDetailRepository: ProductDetailRepository,
    private val setInterestUseCase: SetInterestUseCase,
    private val getPhoneVerificationStatus: GetPhoneVerificationStatusUseCase,
    private val exploreTracker: ExploreTracker,
    private val reportTracker: ReportTracker,
) : ViewModel() {
    private val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)
    private val _productDetail = MutableStateFlow<UiState<ProductDetail>>(UiState.Loading)
    val productDetail = _productDetail.asStateFlow()
    private val _interestFlow = MutableSharedFlow<Boolean>()
    private val _sideEffect = Channel<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()
    private var isProductLoaded = false

    private val _isPhoneVerified = MutableStateFlow(true)
    val isPhoneVerified = _isPhoneVerified.asStateFlow()

    val showProductStatusTooltip = productDetailRepository.getShowProductStatusTooltipFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        if (productId != null) {
            getProductDetail(productId)
            collectAndSetIsInterested(productId)
        } else {
            _productDetail.value = UiState.Failure("상품 정보를 불러올 수 없습니다.")
        }
    }

    fun checkPhoneVerification() = viewModelScope.launch {
        getPhoneVerificationStatus()
            .onSuccess { isVerified ->
                _isPhoneVerified.value = isVerified
            }
    }

    private fun getProductDetail(productId: Long) = viewModelScope.launch {
        productDetailRepository.getProductDetail(productId)
            .onSuccess { response ->
                _productDetail.update { UiState.Success(response) }
                triggerGetProductDetailSideEffect()
            }
            .onFailure {
                Timber.e(it)
                _productDetail.value = UiState.Failure(it.toString())
            }
    }

    private fun triggerGetProductDetailSideEffect() {
        if (!isProductLoaded) {
            trackViewedProduct()
            isProductLoaded = true
        }
    }

    private fun collectAndSetIsInterested(productId: Long) = viewModelScope.launch {
        _interestFlow
            .debounce(DEBOUNCE_DELAY)
            .distinctUntilChanged()
            .collectLatest { isInterested ->
                setInterestUseCase(productId, isInterested)
            }
    }

    fun updateIsInterested(isInterested: Boolean) = viewModelScope.launch {
        updateInterestAndCount(isInterested)
        triggerUpdateIsInterestedSideEffect(isInterested)
    }

    private fun updateInterestAndCount(isInterested: Boolean) {
        val increaseCount = if (isInterested) 1 else -1
        _productDetail.update { currentState ->
            if (currentState is UiState.Success) {
                val updatedState = currentState.data.copy(
                    isInterested = isInterested,
                    interestCount = currentState.data.interestCount + increaseCount
                )
                UiState.Success(updatedState)
            } else {
                currentState
            }
        }
    }

    private suspend fun triggerUpdateIsInterestedSideEffect(isInterested: Boolean) {
        _interestFlow.emit(isInterested)
        if (isInterested) _sideEffect.send(ShowToast(ProductDetailToastType.LIKE))
        else _sideEffect.send(CancelToast)
    }


    fun updateTradeStatus(productId: Long, tradeStatus: String) = viewModelScope.launch {
        productDetailRepository.patchTradeStatus(productId, tradeStatus)
            .onSuccess { updateStatusOnPatchSuccess(tradeStatus) }
            .onFailure(Timber::e)
    }

    private suspend fun updateStatusOnPatchSuccess(tradeStatus: String) {
        var updatedDetail: ProductDetail? = null
        _productDetail.update { currentState ->
            if (currentState is UiState.Success) {
                updatedDetail = currentState.data.copy(tradeStatus = tradeStatus)
                UiState.Success(updatedDetail)
            } else {
                currentState
            }
        }
        updatedDetail?.let { triggerUpdateTradeStatusChangeSideEffect(it) }
    }

    private suspend fun triggerUpdateTradeStatusChangeSideEffect(productDetail: ProductDetail) {
        val tradeType = TradeType.fromName(productDetail.tradeType)
        val tradeStatusType = TradeStatusType.get(productDetail.tradeStatus, tradeType)

        _sideEffect.send(
            ShowToast(
                productDetailToastType = ProductDetailToastType.STATUS_CHANGE,
                message = tradeStatusType.label,
            )
        )
    }

    fun deleteProduct(productId: Long) = viewModelScope.launch {
        productDetailRepository.deleteProduct(productId)
            .onSuccess {
                _sideEffect.send(NavigateUp)
                _sideEffect.send(ShowToast(ProductDetailToastType.DELETE))
            }
            .onFailure(Timber::e)
    }

    fun setShowProductStatusToolTip(value: Boolean) = viewModelScope.launch {
        productDetailRepository.setShowProductStatusTooltip(value)
    }

    private fun trackViewedProduct() {
        val currentUiState = productDetail.value
        if (currentUiState is UiState.Success) {
            val isForSale = TradeType.fromName(currentUiState.data.tradeType) == TradeType.SELL
            exploreTracker.trackViewedProduct(
                postId = currentUiState.data.productId,
                isForSale = isForSale,
            )
        }
    }

    internal fun trackStartedChat(productId: Long) {
        val currentUiState = productDetail.value
        if (currentUiState is UiState.Success) {
            val isForSale = TradeType.fromName(currentUiState.data.tradeType) == TradeType.SELL
            exploreTracker.trackStartedChatFromExplore(
                postId = productId,
                isForSale = isForSale,
            )
        }
    }

    internal fun trackReportProduct() = reportTracker.trackOpenedReportProduct()

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val PRODUCT_ID_KEY = "productId"
    }
}
