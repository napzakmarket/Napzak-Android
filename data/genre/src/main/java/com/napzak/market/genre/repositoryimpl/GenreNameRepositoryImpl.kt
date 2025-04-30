package com.napzak.market.genre.repositoryimpl

import com.napzak.market.genre.datasource.GenreNameDataSource
import com.napzak.market.genre.mapper.toGenres
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.GenreNameRepository
import javax.inject.Inject

class GenreNameRepositoryImpl @Inject constructor(
    private val genreNameDataSource: GenreNameDataSource,
) : GenreNameRepository {
    override suspend fun getGenreNames(cursor: Long?): Result<List<Genre>> =
        runCatching {
            val responseData = genreNameDataSource.getGenreNames(cursor = cursor).data
            responseData.toGenres()
        }

    override suspend fun getGenreNameResults(
        searchWord: String,
        cursor: Long?,
    ): Result<List<Genre>> =
        runCatching {
            val responseData = genreNameDataSource.getGenreNameResults(
                searchWord = searchWord,
                cursor = cursor,
            ).data
            responseData.toGenres()
        }

}
