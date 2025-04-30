package com.napzak.market.genre.repositoryimpl

import com.napzak.market.genre.datasource.PreferredGenreDataSource
import com.napzak.market.genre.mapper.toGenres
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.PreferredGenreRepository
import javax.inject.Inject

class PreferredGenreRepositoryImpl @Inject constructor(
    private val preferredGenreDataSource: PreferredGenreDataSource,
) : PreferredGenreRepository {
    override suspend fun getPreferredGenres(cursor: Long?): Result<List<Genre>> =
        runCatching {
            val responseData = preferredGenreDataSource.getPreferredGenres(cursor = cursor).data
            responseData.toGenres()
        }

    override suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: Long?,
    ): Result<List<Genre>> =
        runCatching {
            val responseData = preferredGenreDataSource.getPreferredGenreResults(
                searchWord = searchWord,
                cursor = cursor
            ).data
            responseData.toGenres()
        }
}
