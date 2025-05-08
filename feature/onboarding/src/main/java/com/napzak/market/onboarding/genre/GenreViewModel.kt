package com.napzak.market.onboarding.genre

import androidx.lifecycle.ViewModel
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    fun onGenreClick(item: GenreUiModel): Boolean {
        var changed = false

        _uiState.update { state ->
            val isAlreadySelected = item.isSelected
            val canSelectMore = state.selectedGenres.size < 7

            val updatedGenres = state.genres.map {
                if (it.name == item.name) {
                    when {
                        isAlreadySelected -> {
                            changed = true
                            it.copy(isSelected = false)
                        }

                        canSelectMore -> {
                            changed = true
                            it.copy(isSelected = true)
                        }

                        else -> it
                    }
                } else it
            }

            state.copy(genres = updatedGenres)
        }

        return changed
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