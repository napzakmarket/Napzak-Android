package com.napzak.market.registration.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.usecase.GetGenreNamesUseCase
import com.napzak.market.registration.genre.state.GenreContract.GenreSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class GenreSearchViewModel @Inject constructor(
    private val getGenreNamesUseCase: GetGenreNamesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenreSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    init {
        viewModelScope.launch {
            searchTerm.debounce(DEBOUNCE_DELAY).collect {
                updateGenreSearchResult()
            }
        }
    }

    fun updateSelectedGenre(genreId: Long?) = _uiState.update {
        it.copy(selectedGenreId = genreId)
    }

    fun updateSearchTerm(searchTerm: String) = _searchTerm.update { searchTerm }

    fun updateUiState(uiState: UiState<Unit>) = _uiState.update {
        it.copy(loadState = uiState)
    }

    private fun updateGenreSearchResult() = viewModelScope.launch {
        updateUiState(UiState.Loading)
        getGenreNamesUseCase(searchTerm.value).onSuccess { genres ->
            _uiState.update { it.copy(genres = genres.map {
                Genre(it.genreId, it.genreName)
            } .toPersistentList()) }
            updateUiState(UiState.Success(Unit))
        }.onFailure {
            updateUiState(UiState.Failure(GENRE_LOAD_ERROR))
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val GENRE_LOAD_ERROR = "failed to load genres"
    }
}
