package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.StoreDetailResponse
import com.napzak.market.store.dto.StoreEditProfileResponse
import com.napzak.market.store.dto.StoreResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface StoreService {
    @GET("stores/mypage")
    suspend fun getStoreInfo(): BaseResponse<StoreResponse>

    @GET("stores/modify/profile")
    suspend fun getStoreEditProfile(): BaseResponse<StoreEditProfileResponse>

    @GET("stores/{storeId}")
    suspend fun getStoreDetail(): BaseResponse<StoreDetailResponse>

    @PUT("stores/modify/profile")
    suspend fun updateStoreProfile(): BaseResponse<StoreEditProfileResponse>
}