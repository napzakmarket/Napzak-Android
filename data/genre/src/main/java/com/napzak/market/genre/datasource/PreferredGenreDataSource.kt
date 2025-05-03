package com.napzak.market.genre.datasource

import com.napzak.market.genre.service.PreferredGenreService
import javax.inject.Inject

class PreferredGenreDataSource @Inject constructor(
    private val preferredGenreService: PreferredGenreService,
) {
    suspend fun getPreferredGenres(cursor: Long?) =
        preferredGenreService.getPreferredGenres(cursor = cursor)

    suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: Long?,
    ) = preferredGenreService.getPreferredGenreResults(
        searchWord = searchWord,
        cursor = cursor,
    )
}
