package com.napzak.market.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.search.state.SearchRecommendation
import com.napzak.market.search.state.SearchUiState
import com.napzak.market.search.state.SearchWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    fun updateRecommendedSearchWordGenres() = viewModelScope.launch {
        // TODO: 추천 검색어/장르 API 연결
        with(uiState.value) {
            updateLoadState(
                UiState.Success(
                    SearchRecommendation(
                        recommendedSearchWords = listOf(
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                            SearchWord(0, "짱구0"),
                        ),
                        recommendedGenres = listOf(
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                            Genre(0, "짱구1234567891011"),
                        ),
                    )
                )
            )
        }
    }

    fun updateSearchTerm(searchTerm: String) {
        _searchTerm.update { searchTerm }
    }

    fun updateSearchResult() = viewModelScope.launch {
        _searchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { debounce ->
                val newResults: List<Genre> = if (debounce.isBlank()) {
                    emptyList()
                } else {
                    // TODO: 검색 API 연결
                    listOf(
                        Genre(0, "짱구2"),
                        Genre(0, "짱구1"),
                    )
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        searchResults = newResults,
                    )
                }
            }
    }

    private fun updateLoadState(loadState: UiState<SearchRecommendation>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }
}