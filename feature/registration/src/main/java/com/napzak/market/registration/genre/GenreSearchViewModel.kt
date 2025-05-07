package com.napzak.market.registration.genre

import androidx.lifecycle.ViewModel
import com.napzak.market.registration.genre.state.GenreContract.GenreSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GenreSearchViewModel @Inject constructor(
    // TODO: API 연결
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenreSearchUiState())
    val uiState = _uiState.asStateFlow()

    fun updateSearchTerm(searchTerm: String) = _uiState.update { currentState ->
        currentState.copy(searchTerm = searchTerm)
    }

    fun debounce() {
        // TODO: API 연결
    }

    fun searchGenre() {
        // TODO: API 연결
    }

}