package com.napzak.market.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeType
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductInterestRepository
import com.napzak.market.ui_util.groupBy
import com.napzak.market.wishlist.state.WishListProducts
import com.napzak.market.wishlist.state.WishlistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    private val productInterestRepository: ProductInterestRepository,
    private val setInterestProductUseCase: SetInterestProductUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<WishlistSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var lastSuccessfulLoadedProducts: Pair<List<Product>, String?> =
        emptyList<Product>() to null
    private val interestDebounceFlow = MutableSharedFlow<Pair<Long, Boolean>>()

    init {
        handleInterestDebounce()
    }

    fun updateWishlistInformation() = viewModelScope.launch {
        when (_uiState.value.selectedTab) {
            TradeType.SELL -> {
                productInterestRepository.getInterestSellProducts(null)
                    .onSuccess {
                        updateLoadState(UiState.Success(WishListProducts(it.first, it.second)))
                        lastSuccessfulLoadedProducts = it
                    }
                    .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
            }

            TradeType.BUY -> {
                productInterestRepository.getInterestBuyProducts(null)
                    .onSuccess {
                        updateLoadState(UiState.Success(WishListProducts(it.first, it.second)))
                        lastSuccessfulLoadedProducts = it
                    }
                    .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
            }
        }
    }

    fun updateTradeType(newTradeType: TradeType) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTab = newTradeType,
            )
        }
    }

    private fun updateLoadState(loadState: UiState<WishListProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState,
            )
        }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun handleInterestDebounce() = viewModelScope.launch {
        interestDebounceFlow
            .groupBy { it.first }
            .flatMapMerge { (_, flow) -> flow.debounce(DEBOUNCE_DELAY) }
            .collect { (productId, finalState) ->
                val originalState = lastSuccessfulLoadedProducts.first
                    .firstOrNull { it.productId == productId }
                    ?.isInterested

                if (originalState != finalState) return@collect //원래 값과 동일한 값이 되면 api 호출 생략

                setInterestProductUseCase(productId, finalState)
                    .onSuccess { updateWishlistInformation() }
                    .onFailure {
                        Timber.e(it.message.toString())
                        updateLoadState(
                            UiState.Success(
                                WishListProducts(
                                    interestProducts = lastSuccessfulLoadedProducts.first,
                                    nextCursor = lastSuccessfulLoadedProducts.second,
                                )
                            )
                        )
                    }
            }
    }

    fun updateProductIsInterested(productId: Long, isInterested: Boolean) = viewModelScope.launch {
        val state = uiState.value.loadState
        when (state) {
            is UiState.Success -> {
                val updatedProducts = state.data.interestProducts.map { product ->
                    if (product.productId == productId) {
                        interestDebounceFlow.emit(productId to isInterested)
                        product.copy(isInterested = !product.isInterested)
                    } else {
                        product
                    }
                }

                val newState =
                    WishListProducts(updatedProducts, state.data.nextCursor)
                updateLoadState(UiState.Success(newState))

                when (isInterested) {
                    true -> _sideEffect.send(WishlistSideEffect.CancelToast)
                    false -> _sideEffect.send(WishlistSideEffect.ShowHeartToast)
                }
            }

            else -> {}
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }
}
