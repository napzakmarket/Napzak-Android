package com.napzak.market.explore.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetGenre
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.bottomsheet.SortBottomSheet
import com.napzak.market.designsystem.component.bottomsheet.rememberGenreBottomSheetState
import com.napzak.market.explore.state.ExploreBottomSheetState
import com.napzak.market.genre.model.Genre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreBottomSheetScreen(
    exploreBottomSheetState: ExploreBottomSheetState,
    sheetState: SheetState,
    selectedGenres: List<Genre>,
    genreItems: List<Genre>,
    sortType: SortType,
    onDismissRequest: (BottomSheetType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
) {
    with(exploreBottomSheetState) {
        if (isGenreSearchingBottomSheetVisible) {
            val genreBottomSheetState = rememberGenreBottomSheetState(
                initiallySelectedGenres = selectedGenres.mapToBottomSheetGenre(),
            )

            LaunchedEffect(genreItems) {
                genreBottomSheetState.genreItems = genreItems.mapToBottomSheetGenre()
            }

            GenreSearchBottomSheet(
                genreBottomSheetState = genreBottomSheetState,
                onDismissRequest = { onDismissRequest(BottomSheetType.GENRE_SEARCHING) },
                onTextChange = onTextChange,
                onButtonClick = { newGenres ->
                    onGenreSelectButtonClick(newGenres.mapToGenre())
                },
            )
        }

        if (isSortBottomSheetVisible) {
            SortBottomSheet(
                selectedSortType = sortType,
                sheetState = sheetState,
                onDismissRequest = { onDismissRequest(BottomSheetType.SORT) },
                onSortItemClick = { newSortOption ->
                    onSortItemClick(newSortOption)
                },
            )
        }
    }
}

private fun List<Genre>.mapToBottomSheetGenre() = map {
    BottomSheetGenre(
        id = it.genreId,
        name = it.genreName,
    )
}

private fun List<BottomSheetGenre>.mapToGenre() = map {
    Genre(
        genreId = it.id,
        genreName = it.name,
    )
}