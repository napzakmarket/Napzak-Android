package com.napzak.market.explore.genredetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.genredetail.state.GenreDetailProducts
import com.napzak.market.explore.genredetail.state.GenreDetailUiState
import com.napzak.market.genre.repository.GenreInfoRepository
import com.napzak.market.interest.repository.InterestProductRepository
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.repository.ProductExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val genreInfoRepository: GenreInfoRepository,
    private val productExploreRepository: ProductExploreRepository,
    private val interestProductRepository: InterestProductRepository,
) : ViewModel() {
    val genreId = savedStateHandle.get<Long>(GENRE_ID_KEY) ?: 0

    private val _uiState = MutableStateFlow(GenreDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun updateGenreInfo() = viewModelScope.launch {
        genreInfoRepository.getGenreInfo(genreId)
            .onSuccess { _uiState.update { currentState -> currentState.copy(genreInfo = it) } }
            .onFailure(Timber::e)
    }

    fun updateGenreDetailInformation() = viewModelScope.launch {
        with(uiState.value) {
            val parameters = ExploreParameters(
                sort = sortOption.toString(),
                genreIds = listOf(genreId),
                isOnSale = isSoldOutSelected,
                isUnopened = isUnopenSelected,
                cursor = null, // TODO: 추후 cursor 값 변경
            )

            when (selectedTab) {
                TradeType.BUY -> {
                    productExploreRepository.getExploreBuyProducts(parameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    GenreDetailProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }

                TradeType.SELL -> {
                    productExploreRepository.getExploreSellProducts(parameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    GenreDetailProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }

                else -> {}
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

    fun updateUnopenFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isUnopenSelected = !_uiState.value.isUnopenSelected,
            )
        }
    }

    fun updateSoldOutFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isSoldOutSelected = !_uiState.value.isSoldOutSelected,
            )
        }
    }

    fun updateSortOption(newSortOption: SortType) {
        _uiState.update { currentState ->
            currentState.copy(
                sortOption = newSortOption,
            )
        }
    }

    fun updateProductIsInterested(productId: Long, isLiked: Boolean) = viewModelScope.launch {
        val state = uiState.value.loadState
        when (state) {
            is UiState.Success -> {
                val updatedProducts = state.data.productList.map { product ->
                    if (product.productId == productId) {
                        product.copy(isInterested = !product.isInterested)
                    } else {
                        product
                    }
                }

                updateLoadState(
                    loadState = UiState.Success(
                        GenreDetailProducts(state.data.productCount, updatedProducts)
                    )
                )
            }

            else -> {}
        }

        if (isLiked) {
            interestProductRepository.unsetInterestProduct(productId)
        } else {
            interestProductRepository.setInterestProduct(productId)
        }
    }

    private fun updateLoadState(loadState: UiState<GenreDetailProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState
            )
        }

    companion object {
        private const val GENRE_ID_KEY = "genreId"
    }
}
