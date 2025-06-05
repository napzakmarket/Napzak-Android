package com.napzak.market.util.android

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setTokens(accessToken: String, refreshToken: String)
    suspend fun updateAccessToken(token: String?)
    suspend fun updateRefreshToken(token: String?)
    suspend fun reissueAccessToken(): String?
    suspend fun clearTokens()
}