package com.napzak.market.genre.datasource

import com.napzak.market.genre.service.GenreInfoService
import javax.inject.Inject

class GenreInfoDataSource @Inject constructor(
    private val genreInfoService: GenreInfoService,
) {
    suspend fun getGenreInfo(genreId: Long) =
        genreInfoService.getGenreInfo(genreId = genreId)
}
