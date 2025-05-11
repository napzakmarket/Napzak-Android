package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.request.KakaoLoginRequest
import com.napzak.market.store.dto.response.KakaoLoginResponse
import com.napzak.market.store.dto.response.ReissueResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("stores/login/kakao")
    suspend fun loginWithKakao(
        @Query("accessToken") accessToken: String,
        @Body body: KakaoLoginRequest
    ): BaseResponse<KakaoLoginResponse>

    @POST("stores/refresh-token")
    suspend fun reissueAccessToken(
        @Header("Cookie") refreshToken: String,
    ): BaseResponse<ReissueResponse>
}