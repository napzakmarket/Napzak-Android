package com.napzak.market.store.repositoryimpl

import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.store.repository.TokenProvider
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : TokenProvider {

    override suspend fun getAccessToken(): String? = tokenDataStore.getAccessToken()

    override suspend fun getRefreshToken(): String? = tokenDataStore.getRefreshToken()

    override suspend fun setTokens(accessToken: String, refreshToken: String) {
        tokenDataStore.setTokens(accessToken, refreshToken)
    }

    override suspend fun clear() {
        tokenDataStore.clearInfo()
    }
}