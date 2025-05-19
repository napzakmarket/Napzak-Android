package com.napzak.market.store.repository

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setTokens(accessToken: String, refreshToken: String)
    suspend fun clear()
}