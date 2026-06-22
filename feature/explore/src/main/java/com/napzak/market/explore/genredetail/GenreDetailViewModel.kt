package com.napzak.market.explore.genredetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.genredetail.state.GenreDetailProducts
import com.napzak.market.explore.genredetail.state.GenreDetailUiState
import com.napzak.market.genre.repository.GenreInfoRepository
import com.napzak.market.product.model.Product
import com.napzak.market.interest.repository.InterestProductRepository
import com.napzak.market.mixpanel.GlobalTracker
import com.napzak.market.navigation.keys.GenreDetailScreenKey
import com.napzak.market.navigation.util.AssistedNavKeyFactory
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.repository.ProductExploreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = GenreDetailViewModel.Factory::class)
class GenreDetailViewModel @AssistedInject constructor(
    @Assisted val navKey: GenreDetailScreenKey,
    private val genreInfoRepository: GenreInfoRepository,
    private val productExploreRepository: ProductExploreRepository,
    private val interestProductRepository: InterestProductRepository,
    private val globalTracker: GlobalTracker,
) : ViewModel() {
    @AssistedFactory
    interface Factory : AssistedNavKeyFactory<GenreDetailViewModel, GenreDetailScreenKey>

    val genreId = navKey.genreId

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
                var trackedProduct: Product? = null
                val updatedProducts = state.data.productList.map { product ->
                    if (product.productId == productId) {
                        trackedProduct = product
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

                trackedProduct?.let { product ->
                    val isForSale = TradeType.fromName(product.tradeType) == TradeType.SELL
                    val actionType = if (isLiked) GlobalTracker.ACTION_REMOVE else GlobalTracker.ACTION_ADD
                    globalTracker.trackItemLiked(
                        postId = productId,
                        genreName = product.genreName,
                        isForSale = isForSale,
                        source = GlobalTracker.SOURCE_GENRE_PAGE,
                        actionType = actionType,
                    )
                }
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

}
