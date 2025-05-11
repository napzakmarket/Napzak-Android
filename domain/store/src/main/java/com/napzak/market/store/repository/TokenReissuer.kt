package com.napzak.market.store.repository

import com.napzak.market.store.model.KakaoLogin

interface TokenReissuer {
    suspend fun loginWithKakao(accessToken: String): Result<KakaoLogin>

    suspend fun reissueAccessToken(refreshToken: String): Result<String>

    suspend fun reissue(refreshToken: String): String
}