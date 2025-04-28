package com.napzak.market.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.explore.model.Product
import com.napzak.market.explore.state.ExploreBottomSheetState
import com.napzak.market.explore.state.ExploreProducts
import com.napzak.market.explore.state.ExploreUiState
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
internal class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
        with(uiState.value) {
            // TODO : 추후 API로 변경
            updateLoadState(
                UiState.Success(
                    ExploreProducts(productList = Product.mockMixedProduct)
                )
            )
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
                    Genre(6, "산리오2"),
                    Genre(7, "주술회전2"),
                    Genre(8, "진격의 거인2"),
                    Genre(9, "산리오3"),
                    Genre(10, "주술회전3"),
                ),
                genreSearchResultItems = listOf(
                    Genre(0, "산리오"),
                    Genre(1, "주술회전"),
                    Genre(2, "진격의 거인"),
                    Genre(3, "산리오1"),
                    Genre(4, "주술회전1"),
                    Genre(5, "진격의 거인1"),
                    Genre(6, "산리오2"),
                    Genre(7, "주술회전2"),
                    Genre(8, "진격의 거인2"),
                    Genre(9, "산리오3"),
                    Genre(10, "주술회전3"),
                )
            )
        }
    }

    fun updateGenreSearchTerm(searchTerm: String) {
        _genreSearchTerm.update { searchTerm }
    }

    @OptIn(FlowPreview::class)
    fun updateGenreSearchResult() = viewModelScope.launch {
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

    // 좋아요 버튼 기능

    private fun updateLoadState(loadState: UiState<ExploreProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val SEARCH_TERM_KEY = "searchTerm"
    }
}