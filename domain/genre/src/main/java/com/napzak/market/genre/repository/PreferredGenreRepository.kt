package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.PreferredGenreSearchItems

interface PreferredGenreRepository {
    suspend fun getPreferredGenres(cursor: String? = null): Result<Pair<List<Genre>, String?>>
    suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: String? = null,
    ): Result<PreferredGenreSearchItems>
}
