package com.napzak.market.genre.repository

import com.napzak.market.genre.model.RecommendedSearchWordGenre

interface SearchWordGenreRepository {
    suspend fun getRecommendedSearchWordGenres() : Result<RecommendedSearchWordGenre>
}
