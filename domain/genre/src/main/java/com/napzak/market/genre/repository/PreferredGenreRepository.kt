package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.PreferredGenreSearchItems

interface PreferredGenreRepository {
    suspend fun getPreferredGenres(cursor: Long? = null): Result<Pair<List<Genre>, Long?>>
    suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: Long? = null,
    ): Result<PreferredGenreSearchItems>
}
