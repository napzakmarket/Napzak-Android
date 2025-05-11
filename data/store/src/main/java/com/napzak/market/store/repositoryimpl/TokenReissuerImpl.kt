package com.napzak.market.store.repositoryimpl

import android.util.Log
import com.napzak.market.store.datasource.AuthDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.KakaoLogin
import com.napzak.market.store.repository.TokenReissuer
import javax.inject.Inject

class TokenReissuerImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : TokenReissuer {
    override suspend fun loginWithKakao(accessToken: String): Result<KakaoLogin> = runCatching {
        authDataSource.loginWithKakao(accessToken).toDomain()
    }

    override suspend fun reissueAccessToken(refreshToken: String): Result<String> = runCatching {
        authDataSource.reissueAccessToken("refreshToken=$refreshToken").accessToken
    }

    override suspend fun reissue(refreshToken: String): String {
        val cookie = "refreshToken=$refreshToken"
        return authDataSource.reissueAccessToken(cookie).accessToken
    }
}