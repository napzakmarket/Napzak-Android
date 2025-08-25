package com.napzak.market.store.usecase

import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
) {
    suspend operator fun invoke(
        accessToken: String,
        refreshToken: String? = null,
    ): Result<Unit> = runCatching {
        if (refreshToken == null) {
            tokenProvider.setTokens(accessToken, tokenProvider.getRefreshToken() ?: "")
        } else {
            tokenProvider.setTokens(accessToken, refreshToken)
        }
    }
}