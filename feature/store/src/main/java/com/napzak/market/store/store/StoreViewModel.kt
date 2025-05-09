package com.napzak.market.store.store

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.store.model.Product
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.store.state.StoreBottomSheetState
import com.napzak.market.store.store.state.StoreInformation
import com.napzak.market.store.store.state.StoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val storeId: Long = savedStateHandle.get<Long>(STORE_ID_KEY) ?: 0

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState = _uiState.asStateFlow()
    private val _bottomSheetState: MutableStateFlow<StoreBottomSheetState> =
        MutableStateFlow(StoreBottomSheetState())
    val bottomSheetState: StateFlow<StoreBottomSheetState> = _bottomSheetState.asStateFlow()

    private val _genreSearchTerm = MutableStateFlow("")

    fun updateStoreInformation() = viewModelScope.launch {
        with(uiState.value) {
            // TODO : 추후 API로 변경
            updateLoadState(
                UiState.Success(
                    StoreInformation(
                        storeDetail = StoreDetail.mockStoreInfo,
                    )
                )
            )
        }
        updateStoreProducts()
    }

    fun updateStoreProducts() = viewModelScope.launch {
        var newProductList: List<Product> = Product.mockMixedProduct // TODO : 추후 API로 변경

        if (uiState.value.loadState is UiState.Success) {
            _uiState.update { currentState ->
                val currentInfo =
                    (uiState.value.loadState as UiState.Success<StoreInformation>).data
                val updatedInfo = currentInfo.copy(productList = newProductList)
                currentState.copy(loadState = UiState.Success(updatedInfo))
            }
        }
    }

    fun updateMarketTabType(newTradeType: MarketTab) {
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

            BottomSheetType.STORE_REPORT -> {
                _bottomSheetState.update {
                    it.copy(isStoreReportBottomSheetVisible = !_bottomSheetState.value.isStoreReportBottomSheetVisible)
                }
            }

            else -> {}
        }
    }

    fun updateGenreItemsInBottomSheet() = viewModelScope.launch {
        _uiState.update { currentState ->
            // TODO : 추후 API로 변경
            currentState.copy(
                initGenreItems = listOf(
                    Genre(0, "산리오"),
                    Genre(1, "주술회전"),
                    Genre(2, "진격의 거인"),
                    Genre(3, "산리오1"),
                    Genre(4, "주술회전1"),
                    Genre(5, "진격의 거인1"),
                ),
                genreSearchResultItems = listOf(
                    Genre(0, "산리오"),
                    Genre(1, "주술회전"),
                )
            )
        }
    }

    fun updateGenreSearchTerm(searchTerm: String) {
        _genreSearchTerm.update { searchTerm }
        updateGenreSearchResult()
    }

    @OptIn(FlowPreview::class)
    private fun updateGenreSearchResult() = viewModelScope.launch {
        _genreSearchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { debounce ->
                val newGenreItems: List<Genre> = if (debounce.isBlank()) {
                    _uiState.value.initGenreItems
                } else {
                    emptyList() // TODO: 장르 검색 API 연결
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        genreSearchResultItems = newGenreItems
                    )
                }
            }
    }

    fun updateSelectedGenres(newGenres: List<Genre>) {
        _uiState.update { currentState ->
            currentState.copy(
                filteredGenres = newGenres
            )
        }
    }

    fun updateIsOnSale() {
        _uiState.update { currentState ->
            currentState.copy(
                isOnSale = !_uiState.value.isOnSale
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

    fun updateProductIsInterested(productId: Long, isLiked: Boolean) {
        // TODO: 좋아요 연결 API 설정
    }

    private fun updateLoadState(loadState: UiState<StoreInformation>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val STORE_ID_KEY = "storeId"
    }
}
