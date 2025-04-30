package com.napzak.market.genre.service

import com.napzak.market.genre.dto.GenreInfoResponse
import com.napzak.market.genre.dto.GenreItemsResponse
import com.napzak.market.genre.dto.RecommendedSearchWordGenreResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenreService {
    @GET("onboarding/genres")
    suspend fun getPreferredGenres(
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>

    @GET("onboarding/genres/search")
    suspend fun getPreferredGenreResults(
        @Query("searchWord") searchWord: String,
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>

    @GET("genres")
    suspend fun getGenreNames(
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>

    @GET("genres/search")
    suspend fun getGenreNameResults(
        @Query("searchWord") searchWord: String,
        @Query("cursor") cursor: Long?,
    ): BaseResponse<GenreItemsResponse>

    @GET("genres/detail/{genreId}")
    suspend fun getGenreInfo(
        @Path("genreId") genreId: Long,
    ): BaseResponse<GenreInfoResponse>

    @GET("products/search/recommend")
    suspend fun getRecommendedSearchWordGenres(): BaseResponse<RecommendedSearchWordGenreResponse>
}
