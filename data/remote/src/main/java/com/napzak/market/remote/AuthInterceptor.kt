package com.napzak.market.remote

import com.napzak.market.util.android.StoreStateManager
import com.napzak.market.util.android.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val storeStateManager: StoreStateManager,
    private val reissueToken: suspend () -> String?,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking { tokenProvider.getAccessToken() }

        val newRequest = originalRequest.newBuilder().apply {
            if (!accessToken.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $accessToken")
            }
        }.build()

        val response = chain.proceed(newRequest)

        if (response.code == 401 && !storeStateManager.isDeletingStore()) {
            val newAccessToken = runBlocking { reissueToken() }

            return if (newAccessToken != null) {
                runBlocking {
                    tokenProvider.setTokens(
                        newAccessToken,
                        tokenProvider.getRefreshToken().orEmpty()
                    )
                }

                val retriedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()

                chain.proceed(retriedRequest)
            } else {
                response
            }
        }

        return response
    }
}