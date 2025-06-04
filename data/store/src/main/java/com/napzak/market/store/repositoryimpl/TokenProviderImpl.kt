package com.napzak.market.store.repositoryimpl

import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.store.repository.AuthRepository
import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val authRepository: AuthRepository,
) : TokenProvider {

    override suspend fun getAccessToken(): String? = tokenDataStore.getAccessToken()

    override suspend fun getRefreshToken(): String? = tokenDataStore.getRefreshToken()

    override suspend fun setTokens(accessToken: String, refreshToken: String) {
        tokenDataStore.setTokens(accessToken, refreshToken)
    }

    override suspend fun reissueAccessToken(): String? {
        val refreshToken = getRefreshToken() ?: return null

        return authRepository.reissueAccessToken(refreshToken)
            .onSuccess { newAccessToken ->
                setTokens(newAccessToken, refreshToken)
            }.getOrNull()
    }

    override suspend fun clearAccessToken() {
        tokenDataStore.setAccessToken(null)
    }

    override suspend fun clearAllTokens() {
        tokenDataStore.clearTokens(null, null)
    }
}