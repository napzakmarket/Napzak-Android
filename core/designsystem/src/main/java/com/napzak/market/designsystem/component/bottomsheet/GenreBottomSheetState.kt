package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberGenreBottomSheetState(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    focusManager: FocusManager = LocalFocusManager.current,
    genres: List<BottomSheetGenre> = emptyList(),
    initiallySelectedGenres: List<BottomSheetGenre>,
) = remember {
    GenreBottomSheetState(
        sheetState = sheetState,
        coroutineScope = coroutineScope,
        focusManager = focusManager,
        genres = genres,
        initiallySelectedGenres = initiallySelectedGenres,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
class GenreBottomSheetState(
    val sheetState: SheetState,
    private val coroutineScope: CoroutineScope,
    private val focusManager: FocusManager,
    genres: List<BottomSheetGenre>,
    initiallySelectedGenres: List<BottomSheetGenre>,
) {
    var searchText by mutableStateOf("")
    var genreItems by mutableStateOf(genres)
    var selectedGenres by mutableStateOf(initiallySelectedGenres)
        private set
    var isShownSnackBar by mutableStateOf(false)
        private set

    fun handleGenreClick(genre: BottomSheetGenre, isSelected: Boolean) {
        if (isSelected) {
            removeSelectedGenre(genre.name)
        } else {
            if (selectedGenres.size < GENRE_SELECT_LIMIT) {
                selectedGenres += genre
            } else {
                showLimitSnackBar()
            }
        }
    }

    private fun showLimitSnackBar() {
        isShownSnackBar = true
        coroutineScope.launch {
            delay(2000)
            isShownSnackBar = false
        }
    }

    fun removeSelectedGenre(name: String) {
        selectedGenres = selectedGenres.filterNot { it.name == name }
    }

    fun resetSelectedGenres() {
        selectedGenres = emptyList()
    }

    fun hide(onAfterHide: () -> Unit = {}) {
        coroutineScope
            .launch { sheetState.hide() }
            .invokeOnCompletion {
                focusManager.clearFocus()
                onAfterHide()
            }
    }

    companion object {
        private const val GENRE_SELECT_LIMIT = 7
    }
}

@Immutable
data class BottomSheetGenre(
    val id: Long,
    val name: String,
)