package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.StoreEditProfileResponse
import retrofit2.http.GET

interface StoreEditProfileService {

    @GET("api/v1/stores/modify/profile")
    suspend fun getStoreEditProfile(
    ): BaseResponse<StoreEditProfileResponse>
}