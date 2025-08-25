package com.napzak.market.store.repositoryimpl

import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.store.repository.AuthRepository
import com.napzak.market.ui_util.JwtInspector
import com.napzak.market.util.android.TokenProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val authRepository: AuthRepository,
) : TokenProvider {

    private val mutex = Mutex()

    override suspend fun getAccessToken(): String? =
        tokenDataStore.getAccessToken()

    override suspend fun getRefreshToken(): String? =
        tokenDataStore.getRefreshToken()

    override suspend fun setTokens(accessToken: String, refreshToken: String) {
        tokenDataStore.setTokens(accessToken, refreshToken)
    }

    override suspend fun updateAccessToken(token: String?) {
        tokenDataStore.updateAccessToken(token)
    }

    override suspend fun updateRefreshToken(token: String?) {
        tokenDataStore.updateRefreshToken(token)
    }

    override suspend fun clearTokens() {
        tokenDataStore.clearTokens()
    }

    override suspend fun getAccessTokenRole(): String? =
        JwtInspector.extractRoleString(getAccessToken())

    override suspend fun reissueAccessToken(): String? = mutex.withLock {
        val refresh = tokenDataStore.getRefreshToken()
        if (refresh.isNullOrBlank()) return@withLock null
        val newAccess = authRepository.reissueAccessToken(refresh).getOrNull()
        if (newAccess.isNullOrBlank()) return@withLock null
        val role = JwtInspector.extractRoleString(newAccess)
        return@withLock if (role == "STORE") {
            tokenDataStore.updateAccessToken(newAccess)
            newAccess
        } else {
            tokenDataStore.clearTokens()
            null
        }
    }
}