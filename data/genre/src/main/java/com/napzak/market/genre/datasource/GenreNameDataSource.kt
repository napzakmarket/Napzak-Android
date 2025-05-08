package com.napzak.market.genre.datasource

import com.napzak.market.genre.service.GenreNameService
import javax.inject.Inject

class GenreNameDataSource @Inject constructor(
    private val genreNameService: GenreNameService,
) {
    suspend fun getGenreNames(cursor: String?, size: Int?) =
        genreNameService.getGenreNames(cursor = cursor, size = size)

    suspend fun getGenreNameResults(
        searchWord: String,
        cursor: String?,
    ) = genreNameService.getGenreNameResults(
        searchWord = searchWord,
        cursor = cursor,
    )
}
