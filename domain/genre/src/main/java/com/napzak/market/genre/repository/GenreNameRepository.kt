package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.GenreSearchItems

interface GenreNameRepository {
    suspend fun getGenreNames(
        cursor: String? = null,
        size: Int? = null,
    ): Result<Pair<List<Genre>, String?>>
    suspend fun getGenreNameResults(
        searchWord: String,
        cursor: String? = null,
    ): Result<GenreSearchItems>
}
