package com.napzak.market.registration.genre.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class GenreContract {
    @Immutable
    data class GenreSearchUiState(
        val loadState: UiState<Unit> = UiState.Loading,
        val genres: ImmutableList<Genre> = persistentListOf(),
        val selectedGenreId: Long? = null,
    )
}
