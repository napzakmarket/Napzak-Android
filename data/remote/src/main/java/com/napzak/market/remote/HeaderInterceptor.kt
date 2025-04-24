package com.napzak.market.remote

import com.napzak.market.local.datastore.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addAccessTokenHeader()
            .build()
        return chain.proceed(newRequest)
    }

    private fun Request.Builder.addAccessTokenHeader(): Request.Builder = runBlocking {
        val accessToken = tokenDataStore.getAccessToken()
        addHeader("Authorization", "Bearer $accessToken")
    }
}