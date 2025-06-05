package com.napzak.market.store.repositoryimpl

import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.store.datasource.AuthDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.KakaoLogin
import com.napzak.market.store.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {
    override suspend fun loginWithKakao(accessToken: String): Result<KakaoLogin> =
        runCatching {
            val response = authDataSource.loginWithKakao(accessToken)

            tokenDataStore.setTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )

            response.toDomain()
        }

    override suspend fun reissueAccessToken(refreshToken: String): Result<String> = runCatching {
        authDataSource.reissueAccessToken("refreshToken=$refreshToken").accessToken
    }

    override suspend fun reissue(refreshToken: String): String {
        val cookie = "refreshToken=$refreshToken"
        return authDataSource.reissueAccessToken(cookie).accessToken
    }
}