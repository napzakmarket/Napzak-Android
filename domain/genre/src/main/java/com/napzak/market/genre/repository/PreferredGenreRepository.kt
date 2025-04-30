package com.napzak.market.genre.repository

import com.napzak.market.genre.model.Genre

interface PreferredGenreRepository {
    suspend fun getPreferredGenres() : Result<List<Genre>>
    suspend fun getPreferredGenreResults() : Result<List<Genre>>
}