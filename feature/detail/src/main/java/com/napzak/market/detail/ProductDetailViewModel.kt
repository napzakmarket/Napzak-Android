package com.napzak.market.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.detail.model.ProductDetail
import com.napzak.market.detail.model.ProductPhoto
import com.napzak.market.detail.model.StoreInfo
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
) : ViewModel() {
    private val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)

    // TODO: 더미 데이터. API 연결 후 삭제
    var productDetail by mutableStateOf(ProductDetail.mock)
        private set

    // TODO: 더미 데이터. API 연결 후 삭제
    var productPhotos by mutableStateOf(ProductPhoto.mockList)
        private set

    // TODO: 더미 데이터. API 연결 후 삭제
    var marketInfo by mutableStateOf(StoreInfo.mock)
        private set

    private val _isInterested = MutableStateFlow(ProductDetail.mock.isInterested)
    val isInterested = _isInterested.asStateFlow()

    private val _sideEffect = Channel<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    @OptIn(FlowPreview::class)
    fun debounceIsInterested() = viewModelScope.launch {
        _isInterested.debounce(DEBOUNCE_DELAY)
            .collectLatest { debounced ->
                Timber.tag("ProductDetail").d("isInterested: $debounced")
            }
    }

    fun updateIsInterested(isInterested: Boolean) = _isInterested.update { isInterested }

    fun deleteProduct() = viewModelScope.launch {
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
