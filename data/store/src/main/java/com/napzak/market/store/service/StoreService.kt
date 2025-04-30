package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.StoreResponse
import retrofit2.http.GET

interface StoreService {
    @GET("api/v1/store/mypage")
    suspend fun getStoreInfo(
    ): BaseResponse<StoreResponse>
}