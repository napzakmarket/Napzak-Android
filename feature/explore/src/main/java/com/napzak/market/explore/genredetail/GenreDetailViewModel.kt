package com.napzak.market.explore.genredetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.genredetail.state.GenreDetailProducts
import com.napzak.market.explore.genredetail.state.GenreDetailUiState
import com.napzak.market.explore.genredetail.state.GenreInfo
import com.napzak.market.explore.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val genreId = savedStateHandle.get<Long>(GENRE_ID_KEY)

    private val _uiState = MutableStateFlow(GenreDetailUiState())
    val uiState = _uiState.asStateFlow()
    private val _sortBottomSheetState = MutableStateFlow(false)
    val sortBottomSheetState = _sortBottomSheetState.asStateFlow()

    fun updateGenreInfo() = viewModelScope.launch {
        _uiState.update { currentState ->
            // TODO : 장르 정보 검색 API
            currentState.copy(
                genreInfo = GenreInfo(
                    genreId = 0,
                    genreName = "장르명",
                    tag = null,
                    cover = "",
                )
            )
        }
    }

    fun updateGenreDetailInformation() = viewModelScope.launch {
        with(uiState.value) {
            // TODO : 장르 디테일 목록 조회 API
            updateLoadState(
                UiState.Success(
                    GenreDetailProducts(productList = Product.mockMixedProduct)
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

    fun updateSortBottomSheetVisibility() {
        _sortBottomSheetState.update { !_sortBottomSheetState.value }
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

    fun updateProductIsInterested(productId: Long, isLiked: Boolean) {
        // TODO: 좋아요 연결 API 설정
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