package com.napzak.market.store.datasource

import com.napzak.market.store.dto.request.KakaoLoginRequest
import com.napzak.market.store.dto.response.KakaoLoginResponse
import com.napzak.market.store.dto.response.ReissueResponse
import com.napzak.market.store.service.AuthService
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun loginWithKakao(accessToken: String): KakaoLoginResponse {
        return authService.loginWithKakao(
            accessToken = accessToken,
            body = KakaoLoginRequest(socialType = "KAKAO")
        ).data
    }

    suspend fun reissueAccessToken(refreshToken: String): ReissueResponse {
        return authService.reissueAccessToken(refreshToken).data
    }
}