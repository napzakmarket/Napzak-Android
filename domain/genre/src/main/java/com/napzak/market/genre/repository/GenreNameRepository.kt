package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre

interface GenreNameRepository {
    suspend fun getGenreNames(cursor: Long? = null): Result<List<Genre>>
    suspend fun getGenreNameResults(
        searchWord: String,
        cursor: Long? = null,
    ): Result<List<Genre>>
}
