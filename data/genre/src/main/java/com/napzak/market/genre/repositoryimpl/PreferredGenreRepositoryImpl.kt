package com.napzak.market.genre.repositoryimpl

import com.napzak.market.genre.datasource.PreferredGenreDataSource
import com.napzak.market.genre.mapper.toDomain
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.PreferredGenreSearchItems
import com.napzak.market.genre.repository.PreferredGenreRepository
import javax.inject.Inject

class PreferredGenreRepositoryImpl @Inject constructor(
    private val preferredGenreDataSource: PreferredGenreDataSource,
) : PreferredGenreRepository {
    override suspend fun getPreferredGenres(cursor: String?): Result<Pair<List<Genre>, String?>> =
        runCatching {
            val responseData = preferredGenreDataSource.getPreferredGenres(cursor = cursor).data
            responseData.toDomain()
        }

    override suspend fun getPreferredGenreResults(
        searchWord: String,
        cursor: String?,
    ): Result<PreferredGenreSearchItems> =
        runCatching {
            val responseData = preferredGenreDataSource.getPreferredGenreResults(
                searchWord = searchWord,
                cursor = cursor,
            ).data
            responseData.toDomain()
        }
}
