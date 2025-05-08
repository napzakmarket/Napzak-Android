package com.napzak.market.registration.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.registration.genre.state.GenreContract.GenreSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreSearchViewModel @Inject constructor(
    // TODO: API 연결
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenreSearchUiState())
    val uiState = _uiState.asStateFlow()

    fun updateSearchTerm(searchTerm: String) {
        _uiState.update { currentState ->
            currentState.copy(searchTerm = searchTerm)
        }
        updateGenreSearchResult()
    }

    private fun updateGenreSearchResult() = viewModelScope.launch {
        // TODO: 여기서 Debounce 구현
    }

    fun searchGenre() {
        // TODO: API 연결
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }
}
