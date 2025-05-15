package com.napzak.market.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.interest.usecase.SetInterestProductUseCase
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
) : ViewModel() {
    private val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)

    private val _productDetail: MutableStateFlow<UiState<ProductDetail>> =
        MutableStateFlow(UiState.Empty)
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

    fun getProductDetail() = viewModelScope.launch {
        if (productId != null) {
            productDetailRepository.getProductDetail(productId)
                .onSuccess { response ->
                    _productDetail.update { UiState.Success(response) }
                    _isInterested.update { response.isInterested }
                }
                .onFailure {
                    Timber.e(it)
                    _productDetail.value = UiState.Failure(it.toString())
                }
        }
    }

    fun updateIsInterested(isInterested: Boolean) = _isInterested.update { isInterested }

    private suspend fun setInterested(productId: Long?, isInterested: Boolean) {
        if (initialLoading) {
            initialLoading = false
        } else if (productId != null) {
            setInterestProductUseCase(productId, isInterested)
        }
    }

    fun updateTradeStatus(productId: Long, tradeStatus: String) = viewModelScope.launch {
        productDetailRepository.patchTradeStatus(productId, tradeStatus)
            .onSuccess {
                getProductDetail()
            }
            .onFailure(Timber::e)
    }

    fun deleteProduct(productId: Long) = viewModelScope.launch {
        productDetailRepository.deleteProduct(productId)
            .onSuccess {
                _sideEffect.send(ProductDetailSideEffect.NavigateUp)
            }
            .onFailure(Timber::e)
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val PRODUCT_ID_KEY = "productId"
    }
}
