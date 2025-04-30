package com.napzak.market.genre.service

import com.napzak.market.genre.dto.RecommendedSearchWordGenreResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET

interface SearchWordGenreService {
    @GET("products/search/recommend")
    suspend fun getRecommendedSearchWordGenres(): BaseResponse<RecommendedSearchWordGenreResponse>
}
