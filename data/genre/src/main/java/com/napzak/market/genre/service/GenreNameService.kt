package com.napzak.market.genre.service

import com.napzak.market.genre.dto.GenreItemsResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreNameService {
    @GET("genres")
    suspend fun getGenreNames(
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>

    @GET("genres/search")
    suspend fun getGenreNameResults(
        @Query("searchWord") searchWord: String,
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>
}
