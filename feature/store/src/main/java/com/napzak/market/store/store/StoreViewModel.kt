package com.napzak.market.store.store

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.extractGenreIds
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.product.model.Product
import com.napzak.market.product.usecase.GetStoreProductsUseCase
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.store.store.state.StoreBottomSheetState
import com.napzak.market.store.store.state.StoreOptionState
import com.napzak.market.store.store.state.StoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storeRepository: StoreRepository,
    private val getProductStoreUseCase: GetStoreProductsUseCase,
    private val genreNameRepository: GenreNameRepository,
) : ViewModel() {
    val storeId: Long = savedStateHandle.get<Long>(STORE_ID_KEY) ?: 0

    private val _genreSearchTerm = MutableStateFlow("")
    private val _storeOptionState: MutableStateFlow<StoreOptionState> =
        MutableStateFlow(StoreOptionState())
    val storeOptionState = _storeOptionState.asStateFlow()

    private val _bottomSheetState: MutableStateFlow<StoreBottomSheetState> =
        MutableStateFlow(StoreBottomSheetState())
    val bottomSheetState: StateFlow<StoreBottomSheetState> = _bottomSheetState.asStateFlow()

    private val _storeDetailState = MutableStateFlow<UiState<StoreDetail>>(UiState.Loading)
    private val _storeProductsState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val storeUiState: StateFlow<StoreUiState> = combine(
        _storeDetailState,
        _storeProductsState,
    ) { storeDetailState, storeProductsState ->
        StoreUiState(
            storeDetailState = storeDetailState,
            storeProductsState = storeProductsState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StoreUiState(
            storeDetailState = UiState.Loading,
            storeProductsState = UiState.Loading,
        )
    )

    fun getStoreInformation() {
        updateStoreDetail()
        updateStoreProducts()
    }

    fun updateStoreDetail() = viewModelScope.launch {
        storeRepository.fetchStoreDetail(storeId)
            .onSuccess { _storeDetailState.value = UiState.Success(it) }
            .onFailure { _storeDetailState.value = UiState.Failure(it.message.toString()) }
    }

    fun updateStoreProducts() = viewModelScope.launch {
        with(_storeOptionState.value) {
            val result = getProductStoreUseCase(
                storeId = storeId,
                isMarketTypeSell = selectedTab == MarketTab.SELL,
                filteredGenres = filteredGenres.extractGenreIds(),
                isOnSale = isOnSale,
                sortOption = sortOption.name,
            )

            result
                .onSuccess { (_, it) -> _storeProductsState.value = UiState.Success(it) }
                .onFailure { _storeProductsState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun updateMarketTabType(newTradeType: MarketTab) {
        _storeOptionState.update { currentState ->
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

            BottomSheetType.STORE_REPORT -> {
                _bottomSheetState.update {
                    it.copy(isStoreReportBottomSheetVisible = !_bottomSheetState.value.isStoreReportBottomSheetVisible)
                }
            }

            else -> {}
        }
    }

    fun updateGenreItemsInBottomSheet() = viewModelScope.launch {
        genreNameRepository.getGenreNames(cursor = null)
            .onSuccess { genres ->
                _storeOptionState.update { currentState ->
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
        updateGenreSearchResult()
    }

    @OptIn(FlowPreview::class)
    private fun updateGenreSearchResult() = viewModelScope.launch {
        _genreSearchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { searchTerm ->
                val genreList = if (searchTerm.isBlank()) {
                    _storeOptionState.value.initGenreItems
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

                _storeOptionState.update { currentState ->
                    currentState.copy(genreSearchResultItems = genreList)
                }
            }
    }

    fun updateSelectedGenres(newGenres: List<Genre>) {
        _storeOptionState.update { currentState ->
            currentState.copy(
                filteredGenres = newGenres,
            )
        }
    }

    fun updateIsOnSale() {
        _storeOptionState.update { currentState ->
            currentState.copy(
                isOnSale = !_storeOptionState.value.isOnSale,
            )
        }
    }

    fun updateSortOption(newSortOption: SortType) {
        _storeOptionState.update { currentState ->
            currentState.copy(
                sortOption = newSortOption,
            )
        }
    }

    fun updateProductIsInterested(productId: Long, isLiked: Boolean) {
        // TODO: 좋아요 연결 API 설정
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val STORE_ID_KEY = "storeId"
    }
}
