package com.napzak.market.onboarding.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.usecase.SetPreferredGenreUseCase
import com.napzak.market.genre.usecase.SetSearchPreferredGenresUseCase
import com.napzak.market.mixpanel.MixpanelConstants.ANDROID
import com.napzak.market.mixpanel.MixpanelConstants.COMPLETED_ONBOARDING
import com.napzak.market.mixpanel.MixpanelConstants.GENRES_CATEGORY
import com.napzak.market.mixpanel.MixpanelConstants.GENRES_COUNT
import com.napzak.market.mixpanel.MixpanelConstants.KAKAO
import com.napzak.market.mixpanel.MixpanelConstants.METHOD
import com.napzak.market.mixpanel.MixpanelConstants.PLATFORM
import com.napzak.market.mixpanel.MixpanelConstants.SKIPPED_GENRES
import com.napzak.market.mixpanel.trackEvent
import com.napzak.market.onboarding.genre.model.GenreEvent
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import com.napzak.market.store.usecase.SetRegisterGenres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val mixpanel: MixpanelAPI?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<GenreEvent>()
    val event: SharedFlow<GenreEvent> = _event

    private var searchJob: Job? = null

    fun updatePreferredGenre() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = UiState.Loading) }

            setPreferredGenreUseCase()
                .onSuccess { genres ->
                    updateGenresAllSelection(genres)

                    val selectedGenreMap = _uiState.value.selectedGenres.associateBy { it.id }
                    val genreUiModels = genres.map { genre ->
                        val isSelected = selectedGenreMap.containsKey(genre.genreId)
                        genre.toUiModel(isSelected = isSelected)
                    }

                    _uiState.update {
                        it.copy(isLoading = UiState.Success(genreUiModels))
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = UiState.Failure(
                                e.message ?: "unknown error"
                            ) as UiState<List<GenreUiModel>>
                        )
                    }
                }
        }
    }

    fun onGenreClick(item: GenreUiModel) {
        var changed = false
        val isAlreadySelected = item.isSelected

        _uiState.update { state ->
            val canSelectMore = state.selectedGenres.size < MAX_SELECTED_COUNT

            if (!isAlreadySelected && !canSelectMore) {
                viewModelScope.launch { _event.emit(GenreEvent.MaxSelectionReached) }
            }

            val updatedGenres = state.genres.map {
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
            }

            val updatedSelectedGenres = updateSelectedGenresList(
                selectedGenres = state.selectedGenres,
                clickedItem = item,
                isAlreadySelected = isAlreadySelected,
                canSelectMore = canSelectMore,
            )

            state.copy(
                genres = updatedGenres,
                selectedGenres = updatedSelectedGenres,
            )
        }
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
                .onSuccess {
                    trackOnboarding()
                    onSuccess()
                }
                .onFailure {
                    //TODO 추후 구현
                }
        }
    }

    private fun updateGenresAllSelection(newGenres: List<Genre>) {
        val selected = _uiState.value.selectedGenres.associateBy { it.id }
        val isSearching = _uiState.value.searchText.isNotBlank()

        val updatedList = if (isSearching) {
            newGenres.map { genre ->
                val isSelected = selected.containsKey(genre.genreId)
                genre.toUiModel(isSelected = isSelected)
            }
        } else {
            newGenres.map { genre ->
                val isSelected = selected.containsKey(genre.genreId)
                genre.toUiModel(isSelected = isSelected)
            }
        }

        _uiState.update {
            it.copy(genres = updatedList)
        }
    }

    internal fun trackOnboarding() {
        val props = mapOf(
            METHOD to KAKAO,
            PLATFORM to ANDROID,
            GENRES_COUNT to uiState.value.selectedGenres.size,
            GENRES_CATEGORY to uiState.value.selectedGenres.map { it.name },
        )

        mixpanel?.trackEvent(COMPLETED_ONBOARDING, props)
    }

    internal fun trackGenreSkipped() = mixpanel?.track(SKIPPED_GENRES)

    companion object {
        private const val MAX_SELECTED_COUNT = 7
    }
}
