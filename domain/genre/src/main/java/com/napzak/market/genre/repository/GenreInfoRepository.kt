package com.napzak.market.genre.repository

import com.napzak.market.genre.model.GenreInfo

interface GenreInfoRepository {
    suspend fun getGenreInfo(genreId: Long): Result<GenreInfo>
}
