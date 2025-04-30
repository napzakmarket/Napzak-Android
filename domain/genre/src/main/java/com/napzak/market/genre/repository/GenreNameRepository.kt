package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre

interface GenreNameRepository {
    suspend fun getGenreNames(): Result<List<Genre>>
    suspend fun getGenreNameResults(): Result<List<Genre>>
}
