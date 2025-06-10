package com.napzak.market.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeType
import com.napzak.market.product.model.Product
import com.napzak.market.wishlist.state.WishListProducts
import com.napzak.market.wishlist.state.WishlistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    // TODO : Repository 추가
) : ViewModel() {
    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState = _uiState.asStateFlow()

    fun updateWishlistInformation() = viewModelScope.launch {
        //TODO : API 연결
        updateLoadState(
            UiState.Success(
                WishListProducts(
                    interestProducts = listOf(
                        Product(
                            productId = 201,
                            genreName = "짱구",
                            productName = "피규어",
                            photo = "http=//example.com/photo3.jpg",
                            price = 120000,
                            uploadTime = "3일",
                            tradeType = TradeType.SELL.toString(),
                            tradeStatus = "BEFORE_TRADE",
                            isPriceNegotiable = false,
                            isOwnedByCurrentUser = false,
                            isInterested = true,
                            interestCount = 4,
                            chatCount = 34,
                        ),
                        Product(
                            productId = 201,
                            genreName = "짱구",
                            productName = "피규어",
                            photo = "http=//example.com/photo3.jpg",
                            price = 120000,
                            uploadTime = "3일",
                            tradeType = TradeType.SELL.toString(),
                            tradeStatus = "BEFORE_TRADE",
                            isPriceNegotiable = false,
                            isOwnedByCurrentUser = false,
                            isInterested = true,
                            interestCount = 4,
                            chatCount = 34,
                        ),
                        Product(
                            productId = 201,
                            genreName = "짱구",
                            productName = "피규어",
                            photo = "http=//example.com/photo3.jpg",
                            price = 120000,
                            uploadTime = "3일",
                            tradeType = TradeType.SELL.toString(),
                            tradeStatus = "BEFORE_TRADE",
                            isPriceNegotiable = false,
                            isOwnedByCurrentUser = false,
                            isInterested = true,
                            interestCount = 4,
                            chatCount = 34,
                        ),
                    )
                )
            )
        )
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
}
