package com.napzak.market.store.edit_store.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetGenre
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.bottomsheet.rememberGenreBottomSheetState
import com.napzak.market.genre.model.Genre
import com.napzak.market.store.model.StoreEditGenre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStoreBottomSheetSection(
    initiallySelectedGenre: List<StoreEditGenre>,
    searchedGenres: List<Genre>,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onStoreGenreChange: (List<StoreEditGenre>) -> Unit,
) {
    val genreBottomSheetState = rememberGenreBottomSheetState(
        initiallySelectedGenres = initiallySelectedGenre.map {
            BottomSheetGenre(
                id = it.genreId,
                name = it.genreName,
            )
        }
    )

    LaunchedEffect(searchedGenres) {
        genreBottomSheetState.genreItems = searchedGenres.map {
            BottomSheetGenre(
                id = it.genreId,
                name = it.genreName,
            )
        }
    }

    GenreSearchBottomSheet(
        genreBottomSheetState = genreBottomSheetState,
        onDismissRequest = onDismissRequest,
        onTextChange = onTextChange,
        onButtonClick = { genres ->
            onStoreGenreChange(genres.map { genre ->
                StoreEditGenre(
                    genreId = genre.id,
                    genreName = genre.name,
                )
            })
        },
    )
}