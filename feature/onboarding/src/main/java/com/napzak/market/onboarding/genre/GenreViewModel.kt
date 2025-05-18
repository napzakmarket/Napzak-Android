package com.napzak.market.onboarding.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.genre.usecase.SetPreferredGenreUseCase
import com.napzak.market.genre.usecase.SetSearchPreferredGenresUseCase
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import com.napzak.market.store.usecase.SetRegisterGenres
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
    private val setRegisterGenres: SetRegisterGenres,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun updatePreferredGenre() {
        viewModelScope.launch {
            setPreferredGenreUseCase()
                .onSuccess { genres ->
                    updateGenresAllSelection(genres)
                }
        }
    }

    fun onGenreClick(item: GenreUiModel): Boolean {
        var changed = false

        _uiState.update { state ->
            val isAlreadySelected = item.isSelected
            val canSelectMore = state.selectedGenres.size < MAX_SELECTED_COUNT

            state.copy(
                genres = state.genres.map {
                    if (it.id == item.id) {
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
                },
                selectedGenres = updateSelectedGenresList(
                    selectedGenres = state.selectedGenres,
                    clickedItem = item,
                    isAlreadySelected = isAlreadySelected,
                    canSelectMore = canSelectMore,
                ),
            )
        }

        return changed
    }

    private fun updateSelectedGenresList(
        selectedGenres: List<GenreUiModel>,
        clickedItem: GenreUiModel,
        isAlreadySelected: Boolean,
        canSelectMore: Boolean,
    ): List<GenreUiModel> {
        return when {
            isAlreadySelected -> selectedGenres.filterNot { it.id == clickedItem.id }
            canSelectMore -> selectedGenres + clickedItem
            else -> selectedGenres
        }
    }

    fun onGenreRemove(item: GenreUiModel) {
        _uiState.update { state ->
            state.copy(
                genres = state.genres.map {
                    if (it.id == item.id) it.copy(isSelected = false)
                    else it
                },
                selectedGenres = state.selectedGenres.filterNot { it.id == item.id },
            )
        }
    }

    fun onResetAllGenres() {
        _uiState.update { state ->
            state.copy(
                genres = state.genres.map {
                    if (it.isSelected) it.copy(isSelected = false) else it
                },
                selectedGenres = emptyList(),
            )
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
                    updateGenresAllSelection(genres)
                }
                .onFailure {
                    // TODO: UI에서 메시지 처리
                }
        }
    }

    fun updateSelectedGenres(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val selectedIds = _uiState.value.selectedGenres.map { it.id }
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            setRegisterGenres(selectedIds)
                .onSuccess { onSuccess() }
                .onFailure {
                    //TODO 추후 구현
                }
        }
    }

    private fun updateGenresAllSelection(newGenres: List<com.napzak.market.genre.model.Genre>) {
        val selected = _uiState.value.selectedGenres.associateBy { it.id }

        val merged = newGenres.map { genre ->
            val wasSelected = selected[genre.genreId] != null
            genre.toUiModel(isSelected = wasSelected)
        }

        val additional = selected.values.filterNot { selectedItem ->
            merged.any { it.id == selectedItem.id }
        }

        _uiState.update {
            it.copy(genres = merged + additional)
        }
    }

    companion object{
        private const val MAX_SELECTED_COUNT = 7
    }
}
