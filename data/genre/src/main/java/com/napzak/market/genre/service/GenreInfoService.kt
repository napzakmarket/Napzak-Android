package com.napzak.market.genre.service

import com.napzak.market.genre.dto.GenreInfoResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GenreInfoService {
    @GET("genres/detail/{genreId}")
    suspend fun getGenreInfo(
        @Path("genreId") genreId: Long,
    ): BaseResponse<GenreInfoResponse>
}
