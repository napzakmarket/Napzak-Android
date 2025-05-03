package com.napzak.market.genre.service

import com.napzak.market.genre.dto.PreferredGenreItemsResponse
import com.napzak.market.genre.dto.PreferredGenreSearchItemsResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PreferredGenreService {
    @GET("onboarding/genres")
    suspend fun getPreferredGenres(
        @Query("cursor") cursor: Long?,
    ): BaseResponse<PreferredGenreItemsResponse>

    @GET("onboarding/genres/search")
    suspend fun getPreferredGenreResults(
        @Query("searchWord") searchWord: String,
        @Query("cursor") cursor: Long?,
    ): BaseResponse<PreferredGenreSearchItemsResponse>
}
