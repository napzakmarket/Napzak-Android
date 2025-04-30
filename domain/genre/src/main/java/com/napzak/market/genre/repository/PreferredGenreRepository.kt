package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre

interface PreferredGenreRepository {
    suspend fun getPreferredGenres(cursor: Long? = null): Result<List<Genre>>
    suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: Long? = null,
    ): Result<List<Genre>>
}
