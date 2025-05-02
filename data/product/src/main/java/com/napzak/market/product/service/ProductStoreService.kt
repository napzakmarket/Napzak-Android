package com.napzak.market.product.service

import com.napzak.market.product.dto.ProductExploreBuyResponse
import com.napzak.market.product.dto.ProductExploreSellResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductStoreService {
    @GET("products/sell/stores/{storeId}")
    suspend fun getStoreSellProducts(
        @Path("storeId") storeId: Long,
        @Query("sort") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("isUnopened") isUnopened: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreSellResponse>

    @GET("products/buy/stores/{storeId}")
    suspend fun getStoreBuyProducts(
        @Path("storeId") storeId: Long,
        @Query("sort") sort: String,
        @Query("genreId") genreIds: List<Long>?,
        @Query("isOnSale") isOnSale: Boolean,
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductExploreBuyResponse>
}
