package com.napzak.market.genre.repository

import com.napzak.market.genre.datasource.GenreInfoDataSource
import com.napzak.market.genre.mapper.toDomain
import com.napzak.market.genre.model.GenreInfo
import javax.inject.Inject

class GenreInfoRepositoryImpl @Inject constructor(
    private val genreInfoDataSource: GenreInfoDataSource,
) : GenreInfoRepository {
    override suspend fun getGenreInfo(genreId: Long): Result<GenreInfo> =
        runCatching {
            val responseData = genreInfoDataSource.getGenreInfo(genreId = genreId).data
            responseData.toDomain()
        }

}
