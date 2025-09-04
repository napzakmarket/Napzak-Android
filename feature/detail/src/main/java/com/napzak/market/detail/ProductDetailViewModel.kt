package com.napzak.market.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.detail.type.ProductDetailToastType
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.mixpanel.MixpanelConstants.BUYER
import com.napzak.market.mixpanel.MixpanelConstants.FOR_SALE
import com.napzak.market.mixpanel.MixpanelConstants.POST_ID
import com.napzak.market.mixpanel.MixpanelConstants.POST_TYPE
import com.napzak.market.mixpanel.MixpanelConstants.SELLER
import com.napzak.market.mixpanel.MixpanelConstants.STARTED_CHAT
import com.napzak.market.mixpanel.MixpanelConstants.USER_ROLE
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_PRODUCT
import com.napzak.market.mixpanel.MixpanelConstants.WANTED
import com.napzak.market.mixpanel.trackEvent
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.repository.ProductDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productDetailRepository: ProductDetailRepository,
    private val setInterestProductUseCase: SetInterestProductUseCase,
    private val mixpanel: MixpanelAPI?,
) : ViewModel() {
    private val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)

    private val _productDetail: MutableStateFlow<UiState<ProductDetail>> =
        MutableStateFlow(UiState.Loading)
    val productDetail = _productDetail.asStateFlow()

    // NOTE: 뷰모델이 처음 생성될 때 좋아요 로직이 불리는 것을 방지하기 위해 initialLoading을 사용한다.
    private var initialLoading by mutableStateOf(true)
    private val _isInterested = MutableStateFlow(true)

    @OptIn(FlowPreview::class)
    val isInterested = _isInterested.asStateFlow().apply {
        viewModelScope.launch {
            this@apply.debounce(DEBOUNCE_DELAY).collectLatest { debounced ->
                // NOTE: debounce 처리로 인해 좋아요 조건이 바뀜
                setInterested(productId, !debounced)
            }
        }
    }

    private val _sideEffect = Channel<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var isProductLoaded = false

    fun getProductDetail() = viewModelScope.launch {
        if (productId != null) {
            productDetailRepository.getProductDetail(productId)
                .onSuccess { response ->
                    _productDetail.update { UiState.Success(response) }
                    _isInterested.update { response.isInterested }

                    if (!isProductLoaded) {
                        trackViewedProduct()
                        isProductLoaded = true
                    }
                }
                .onFailure {
                    Timber.e(it)
                    _productDetail.value = UiState.Failure(it.toString())
                }
        }
    }

    fun updateIsInterested(isInterested: Boolean) {
        _isInterested.update { isInterested }
        viewModelScope.launch {
            if (isInterested) {
                _sideEffect.send(ProductDetailSideEffect.ShowToast(ProductDetailToastType.LIKE))
            } else {
                _sideEffect.send(ProductDetailSideEffect.CancelToast)
            }
        }
    }

    private suspend fun setInterested(productId: Long?, isInterested: Boolean) {
        if (initialLoading) {
            initialLoading = false
        } else if (productId != null) {
            setInterestProductUseCase(productId, isInterested).onSuccess {
                updateInterestCount(isInterested)
            }
        }
    }

    private fun updateInterestCount(isInterested: Boolean) {
        val increaseCount = if (isInterested) -1 else 1

        _productDetail.update { uiState ->
            UiState.Success(
                (uiState as UiState.Success).data.copy(
                    interestCount = uiState.data.interestCount + increaseCount
                )
            )
        }
    }

    fun updateTradeStatus(productId: Long, tradeStatus: String) = viewModelScope.launch {
        productDetailRepository.patchTradeStatus(productId, tradeStatus)
            .onSuccess {
                getProductDetail()
                runCatching {
                    val tradeType = TradeType.fromName(
                        (_productDetail.value as UiState.Success<ProductDetail>).data.tradeType
                    )
                    Timber.d("tradeStatus: $tradeStatus")
                    _sideEffect.send(
                        ProductDetailSideEffect.ShowToast(
                            productDetailToastType = ProductDetailToastType.STATUS_CHANGE,
                            message = TradeStatusType.get(tradeStatus, tradeType).label,
                        )
                    )
                }
            }
            .onFailure(Timber::e)
    }

    fun deleteProduct(productId: Long) = viewModelScope.launch {
        productDetailRepository.deleteProduct(productId)
            .onSuccess {
                _sideEffect.send(ProductDetailSideEffect.NavigateUp)
                _sideEffect.send(ProductDetailSideEffect.ShowToast(ProductDetailToastType.DELETE))
            }
            .onFailure(Timber::e)
    }

    private fun trackViewedProduct() {
        val currentUiState = productDetail.value
        if (currentUiState is UiState.Success) {
            val isForSale = TradeType.fromName(currentUiState.data.tradeType) == TradeType.SELL
            val props = mapOf(
                POST_ID to currentUiState.data.productId,
                POST_TYPE to if (isForSale) FOR_SALE else WANTED,
            )
            mixpanel?.trackEvent(VIEWED_PRODUCT, props)
        }
    }

    internal fun trackStartedChat(productId: Long) {
        val currentUiState = productDetail.value
        if (currentUiState is UiState.Success) {
            val isForSale = TradeType.fromName(currentUiState.data.tradeType) == TradeType.SELL
            val props = mapOf(
                POST_ID to productId,
                POST_TYPE to if (isForSale) FOR_SALE else WANTED,
                USER_ROLE to if (isForSale) BUYER else SELLER,
            )
            mixpanel?.trackEvent(STARTED_CHAT, props)
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val PRODUCT_ID_KEY = "productId"
    }
}
