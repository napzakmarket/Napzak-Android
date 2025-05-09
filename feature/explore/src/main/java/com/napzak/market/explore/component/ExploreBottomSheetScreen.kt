package com.napzak.market.explore.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.bottomsheet.SortBottomSheet
import com.napzak.market.explore.state.ExploreBottomSheetState

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
    modifier: Modifier = Modifier,
) {
    with(exploreBottomSheetState) {
        if (isGenreSearchingBottomSheetVisible) {
            GenreSearchBottomSheet(
                initialSelectedGenreList = selectedGenres,
                sheetState = sheetState,
                genreItems = genreItems,
                onDismissRequest = { onDismissRequest(BottomSheetType.GENRE_SEARCHING) },
                onTextChange = onTextChange,
                onButtonClick = { newGenres ->
                    onGenreSelectButtonClick(newGenres)
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