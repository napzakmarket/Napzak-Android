package com.napzak.market.onboarding.genre

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.genre.usecase.SetPreferredGenreUseCase
import com.napzak.market.genre.usecase.SetSearchPreferredGenresUseCase
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val setPreferredGenreUseCase: SetPreferredGenreUseCase,
    private val setSearchPreferredGenresUseCase: SetSearchPreferredGenresUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun updatePreferredGenre() {
        viewModelScope.launch {
            setPreferredGenreUseCase()
                .onSuccess { genres ->
                    _uiState.update {
                        it.copy(genres = genres.map { genre -> genre.toUiModel() })
                    }
                }
        }
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

        searchJob?.cancel()
        if (text.isBlank()) {
            updatePreferredGenre()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            searchPreferredGenres(text)
        }
    }

    fun onSearchTextSubmit() {
        val searchText = _uiState.value.searchText
        if (searchText.isNotBlank()) {
            searchJob?.cancel()
            searchPreferredGenres(searchText)
        }
    }

    private fun searchPreferredGenres(searchText: String) {
        viewModelScope.launch {
            setSearchPreferredGenresUseCase(searchText)
                .onSuccess { genres ->
                    _uiState.update {
                        it.copy(genres = genres.map { it.toUiModel() })
                    }
                }
        }
    }
}