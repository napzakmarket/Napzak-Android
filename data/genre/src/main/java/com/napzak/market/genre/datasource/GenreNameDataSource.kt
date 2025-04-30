package com.napzak.market.genre.datasource

import com.napzak.market.genre.service.GenreNameService
import javax.inject.Inject

class GenreNameDataSource @Inject constructor(
    private val genreNameService: GenreNameService,
) {
    suspend fun getGenreNames(cursor: Long? = null) =
        genreNameService.getGenreNames(cursor = cursor)

    suspend fun getGenreNameResults(
        searchWord: String,
        cursor: Long? = null,
    ) = genreNameService.getGenreNameResults(
        searchWord = searchWord,
        cursor = cursor,
    )
}
