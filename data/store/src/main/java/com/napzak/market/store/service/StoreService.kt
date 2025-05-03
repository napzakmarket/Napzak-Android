package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.request.GenreRegistrationRequest
import com.napzak.market.store.dto.request.NicknameRequest
import com.napzak.market.store.dto.request.WithdrawRequest
import com.napzak.market.store.dto.response.GenreRegistrationResponse
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.dto.response.StoreDetailResponse
import com.napzak.market.store.dto.request.StoreEditProfileRequest
import com.napzak.market.store.dto.response.StoreEditProfileResponse
import com.napzak.market.store.dto.response.StoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StoreService {
    @GET("stores/nickname/check")
    suspend fun getNicknameValidation(
        @Body request: NicknameRequest,
    ): BaseResponse<Unit>

    @POST("stores/nickname/register")
    suspend fun postNicknameRegistration(
        @Body request: NicknameRequest,
    ): BaseResponse<Unit>

    @GET("stores/genres/register")
    suspend fun getGenresRegistration(
        @Body request: GenreRegistrationRequest,
    ): BaseResponse<GenreRegistrationResponse>

    @POST("stores/withdraw")
    suspend fun postWithdraw(
        @Body request: WithdrawRequest,
    ): BaseResponse<WithdrawResponse>

    @POST("stores/logout")
    suspend fun postLogout(): BaseResponse<Unit>

    @GET("stores/mypage")
    suspend fun getStoreInfo(): BaseResponse<StoreResponse>

    @GET("stores/modify/profile")
    suspend fun getStoreEditProfile(): BaseResponse<StoreEditProfileResponse>

    @GET("stores/{storeId}")
    suspend fun getStoreDetail(
        @Path("storeId") storeId: Long
    ): BaseResponse<StoreDetailResponse>

    @PUT("stores/modify/profile")
    suspend fun updateStoreProfile(
        @Body request: StoreEditProfileRequest
    ): BaseResponse<StoreEditProfileResponse>
}