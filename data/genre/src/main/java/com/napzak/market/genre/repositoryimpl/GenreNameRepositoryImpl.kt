package com.napzak.market.genre.repositoryimpl

import com.napzak.market.genre.datasource.GenreNameDataSource
import com.napzak.market.genre.mapper.toDomain
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.GenreSearchItems
import com.napzak.market.genre.repository.GenreNameRepository
import javax.inject.Inject

class GenreNameRepositoryImpl @Inject constructor(
    private val genreNameDataSource: GenreNameDataSource,
) : GenreNameRepository {
    override suspend fun getGenreNames(
        cursor: String?,
        size: Int?,
    ): Result<Pair<List<Genre>, String?>> =
        runCatching {
            val responseData = genreNameDataSource.getGenreNames(cursor = cursor, size = size).data
            responseData.toDomain()
        }

    override suspend fun getGenreNameResults(
        searchWord: String,
        cursor: String?,
    ): Result<GenreSearchItems> =
        runCatching {
            val responseData = genreNameDataSource.getGenreNameResults(
                searchWord = searchWord,
                cursor = cursor,
            ).data
            responseData.toDomain()
        }

}
