package com.napzak.market.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.state.ExploreBottomSheetState
import com.napzak.market.explore.state.ExploreProducts
import com.napzak.market.explore.state.ExploreUiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.extractGenreIds
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.interest.repository.InterestProductRepository
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.SearchParameters
import com.napzak.market.product.repository.ProductExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productExploreRepository: ProductExploreRepository,
    private val genreNameRepository: GenreNameRepository,
    private val interestProductRepository: InterestProductRepository,
) : ViewModel() {
    val searchTerm: String = savedStateHandle.get<String>(SEARCH_TERM_KEY) ?: ""

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()
    private val _bottomSheetState: MutableStateFlow<ExploreBottomSheetState> =
        MutableStateFlow(ExploreBottomSheetState())
    val bottomSheetState: StateFlow<ExploreBottomSheetState> = _bottomSheetState.asStateFlow()

    private val _genreSearchTerm = MutableStateFlow("")
    val genreSearchTerm = _genreSearchTerm.asStateFlow()

    fun updateExploreInformation() = viewModelScope.launch {
        val parameters = with(uiState.value) {
            if (searchTerm.isEmpty()) {
                ExploreParameters(
                    sort = sortOption.toString(),
                    genreIds = filteredGenres.extractGenreIds(),
                    isOnSale = isSoldOutSelected,
                    isUnopened = isUnopenSelected,
                    cursor = null, // TODO: 추후 cursor 값 변경
                )
            } else {
                SearchParameters(
                    searchWord = searchTerm,
                    sort = sortOption.toString(),
                    genreIds = filteredGenres.extractGenreIds(),
                    isOnSale = isSoldOutSelected,
                    isUnopened = isUnopenSelected,
                    cursor = null, // TODO: 추후 cursor 값 변경
                )
            }
        }

        when (uiState.value.selectedTab) {
            TradeType.BUY -> {
                if (searchTerm.isEmpty()) {
                    productExploreRepository.getExploreBuyProducts(parameters as ExploreParameters)
                        .onSuccess { updateLoadState(UiState.Success(ExploreProducts(it.second))) }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                } else {
                    productExploreRepository.getSearchBuyProducts(parameters as SearchParameters)
                        .onSuccess { updateLoadState(UiState.Success(ExploreProducts(it.second))) }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }
            }

            TradeType.SELL -> {
                if (searchTerm.isEmpty()) {
                    productExploreRepository.getExploreSellProducts(parameters as ExploreParameters)
                        .onSuccess { updateLoadState(UiState.Success(ExploreProducts(it.second))) }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                } else {
                    productExploreRepository.getSearchSellProducts(parameters as SearchParameters)
                        .onSuccess { updateLoadState(UiState.Success(ExploreProducts(it.second))) }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }
            }

            else -> {}
        }
    }


    fun updateTradeType(newTradeType: TradeType) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTab = newTradeType,
            )
        }
    }

    fun updateBottomSheetVisibility(type: BottomSheetType) {
        when (type) {
            BottomSheetType.GENRE_SEARCHING -> {
                _bottomSheetState.update {
                    it.copy(isGenreSearchingBottomSheetVisible = !_bottomSheetState.value.isGenreSearchingBottomSheetVisible)
                }
            }

            BottomSheetType.SORT -> {
                _bottomSheetState.update {
                    it.copy(isSortBottomSheetVisible = !_bottomSheetState.value.isSortBottomSheetVisible)
                }
            }

            else -> {}
        }
    }

    fun updateGenreItemsInBottomSheet() = viewModelScope.launch {
        genreNameRepository.getGenreNames(cursor = null)
            .onSuccess { genres ->
                _uiState.update { currentState ->
                    currentState.copy(
                        initGenreItems = genres.first,
                        genreSearchResultItems = genres.first,
                    )
                }
            }
            .onFailure(Timber::e)
    }

    fun updateGenreSearchTerm(searchTerm: String) {
        _genreSearchTerm.update { searchTerm }
    }

    @OptIn(FlowPreview::class)
    fun updateGenreSearchResult() = viewModelScope.launch {
        _genreSearchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { searchTerm ->
                val genreList = if (searchTerm.isBlank()) {
                    _uiState.value.initGenreItems
                } else {
                    genreNameRepository.getGenreNameResults(searchTerm)
                        .fold(
                            onSuccess = { it.genreList },
                            onFailure = {
                                Timber.e(it)
                                emptyList()
                            }
                        )
                }

                _uiState.update { currentState ->
                    currentState.copy(genreSearchResultItems = genreList)
                }
            }
    }

    fun updateSelectedGenres(newGenres: List<Genre>) {
        _uiState.update { currentState ->
            currentState.copy(
                filteredGenres = newGenres,
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
        when (val state = uiState.value.loadState) {
            is UiState.Success -> {
                val updatedProducts = state.data.productList.map { product ->
                    if (product.productId == productId) {
                        product.copy(isInterested = !product.isInterested)
                    } else {
                        product
                    }
                }

                updateLoadState(loadState = UiState.Success(ExploreProducts(updatedProducts)))
            }

            else -> {}
        }

        if (isLiked) {
            interestProductRepository.unsetInterestProduct(productId)
        } else {
            interestProductRepository.setInterestProduct(productId)
        }
    }

    private fun updateLoadState(loadState: UiState<ExploreProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState,
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val SEARCH_TERM_KEY = "searchTerm"
    }
}
