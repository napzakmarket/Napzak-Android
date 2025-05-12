package com.napzak.market.remote

import com.napzak.market.local.datastore.TokenDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking { tokenDataStore.getAccessToken() }

        val authenticatedRequest = originalRequest.newBuilder().apply {
            if (!accessToken.isNullOrBlank()) {
                header("Authorization", "Bearer $accessToken")
            }
        }.build()

        return chain.proceed(authenticatedRequest)
    }
}