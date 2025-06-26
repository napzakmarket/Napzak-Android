package com.napzak.market.registration.genre

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
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
    savedStateHandle: SavedStateHandle,
    private val getGenreNamesUseCase: GetGenreNamesUseCase,
) : ViewModel() {
    private val genreId: Long? = savedStateHandle.get<Long>(GENRE_ID_KEY)

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

    fun updateSelectedGenre() = _uiState.update {
        it.copy(selectedGenreId = genreId)
    }

    fun updateSearchTerm(searchTerm: String) = _searchTerm.update { searchTerm }

    fun updateUiState(uiState: UiState<Unit>) = _uiState.update {
        it.copy(loadState = uiState)
    }

    private fun updateGenreSearchResult() = viewModelScope.launch {
        updateUiState(UiState.Loading)
        getGenreNamesUseCase(searchTerm.value).onSuccess { genres ->
            _uiState.update { it.copy(genres = genres.toPersistentList()) }
            updateUiState(UiState.Success(Unit))
        }.onFailure {
            updateUiState(UiState.Failure("failed to load genres"))
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val GENRE_ID_KEY = "genreId"
    }
}
