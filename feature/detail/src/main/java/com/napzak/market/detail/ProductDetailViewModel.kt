package com.napzak.market.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
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
) : ViewModel() {
    val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)

    private val _productDetail: MutableStateFlow<UiState<ProductDetail>> =
        MutableStateFlow(UiState.Empty)
    val productDetail = _productDetail.asStateFlow()

    private val _isInterested = MutableStateFlow(false)
    val isInterested = _isInterested.asStateFlow()

    private val _sideEffect = Channel<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    suspend fun getProductDetail() {
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

    @OptIn(FlowPreview::class)
    fun debounceIsInterested() = viewModelScope.launch {
        _isInterested.debounce(DEBOUNCE_DELAY)
            .collectLatest { debounced ->
                Timber.tag("ProductDetail").d("isInterested: $debounced")
            }
    }

    fun updateIsInterested(isInterested: Boolean) = _isInterested.update { isInterested }

    fun deleteProduct(productId: Long) = viewModelScope.launch {
        runCatching {
            Timber.tag("ProductDetail").d("delete product")
        }.onSuccess {
            _sideEffect.send(ProductDetailSideEffect.NavigateUp)
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val PRODUCT_ID_KEY = "productId"
    }
}
