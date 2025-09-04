package com.napzak.market.remote

import com.napzak.market.local.datastore.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenDataStore.getAccessToken() }

        val newRequest = chain.request().newBuilder().apply {
            if (!accessToken.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $accessToken")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}