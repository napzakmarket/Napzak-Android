package com.napzak.market.product.service

import com.napzak.market.product.dto.ProductExploreBuyResponse
import com.napzak.market.product.dto.ProductExploreSellResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductExploreService {
    @GET("products/sell")
    suspend fun getExploreSellProducts(
        @Query("sortOption") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("isUnopened") isUnopened: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreSellResponse>

    @GET("products/buy")
    suspend fun getExploreBuyProducts(
        @Query("sort") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreBuyResponse>

    @GET("products/sell/search")
    suspend fun getSearchSellProducts(
        @Query("searchWord") searchWord: String,
        @Query("sort") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("isUnopened") isUnopened: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreSellResponse>

    @GET("products/buy/search")
    suspend fun getSearchBuyProducts(
        @Query("searchWord") searchWord: String,
        @Query("sort") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreBuyResponse>
}
