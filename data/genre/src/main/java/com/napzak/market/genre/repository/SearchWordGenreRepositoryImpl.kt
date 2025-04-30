package com.napzak.market.genre.repository

import com.napzak.market.genre.datasource.SearchWordGenreDataSource
import com.napzak.market.genre.mapper.toDomain
import com.napzak.market.genre.model.RecommendedSearchWordGenre
import javax.inject.Inject

class SearchWordGenreRepositoryImpl @Inject constructor(
    private val searchWordGenreDataSource: SearchWordGenreDataSource,
) : SearchWordGenreRepository {
    override suspend fun getRecommendedSearchWordGenres(): Result<RecommendedSearchWordGenre> =
        runCatching {
            val responseData = searchWordGenreDataSource.getRecommendedSearchWordGenres().data
            responseData.toDomain()
        }
}
