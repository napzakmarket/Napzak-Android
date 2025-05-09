package com.napzak.market.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.genre.repository.SearchWordGenreRepository
import com.napzak.market.search.state.SearchRecommendation
import com.napzak.market.search.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchWordGenreRepository: SearchWordGenreRepository,
    private val genreNameRepository: GenreNameRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    fun updateRecommendedSearchWordGenres() = viewModelScope.launch {
        with(uiState.value) {
            searchWordGenreRepository.getRecommendedSearchWordGenres()
                .onSuccess {
                    updateLoadState(
                        UiState.Success(
                            SearchRecommendation(
                                recommendedSearchWords = it.searchWordList,
                                recommendedGenres = it.genreList,
                            )
                        )
                    )
                }
                .onFailure(Timber::e)
        }
    }

    fun updateSearchTerm(searchTerm: String) {
        _searchTerm.update { searchTerm }
        updateSearchResult()
    }

    @OptIn(FlowPreview::class)
    private fun updateSearchResult() = viewModelScope.launch {
        _searchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { searchTerm ->
                genreNameRepository.getGenreNameResults(searchTerm)
                    .onSuccess {
                        _uiState.update { currentState ->
                            currentState.copy(searchResults = it.genreList)
                        }
                    }
                    .onFailure(Timber::e)
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