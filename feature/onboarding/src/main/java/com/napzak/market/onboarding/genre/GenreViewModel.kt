package com.napzak.market.onboarding.genre

import androidx.lifecycle.ViewModel
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    fun updateGenres(genres: List<GenreUiModel>) {
        _uiState.update { it.copy(genres = genres) }
    }

    fun onGenreClick(genre: GenreUiModel) {
        _uiState.update { state ->
            val updated = state.genres.map {
                if (it.name == genre.name) it.copy(isSelected = !it.isSelected) else it
            }
            state.copy(genres = updated)
        }
    }

    fun onGenreRemove(genre: GenreUiModel) {
        _uiState.update { state ->
            val updated = state.genres.map {
                if (it.name == genre.name) it.copy(isSelected = false) else it
            }
            state.copy(genres = updated)
        }
    }

    fun onResetAllGenres() {
        _uiState.update { state ->
            state.copy(genres = state.genres.map { it.copy(isSelected = false) })
        }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }
}