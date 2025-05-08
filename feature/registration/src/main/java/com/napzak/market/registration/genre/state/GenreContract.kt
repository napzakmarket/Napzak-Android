package com.napzak.market.registration.genre.state

import androidx.compose.runtime.Immutable
import com.napzak.market.genre.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class GenreContract {
    @Immutable
    data class GenreSearchUiState(
        val searchTerm: String = "",
        val genres: ImmutableList<Genre> = persistentListOf(),
    )
}
