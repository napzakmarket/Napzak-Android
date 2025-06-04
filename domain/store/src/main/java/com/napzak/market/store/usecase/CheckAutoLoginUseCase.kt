package com.napzak.market.store.usecase

import com.napzak.market.store.repository.AuthRepository
import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class CheckAutoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenProvider: TokenProvider,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val refreshToken = tokenProvider.getRefreshToken()
            ?: throw IllegalStateException("refreshToken 없음")

        val newAccessToken = authRepository.reissue(refreshToken)

        val oldRefreshToken = refreshToken
        tokenProvider.setTokens(newAccessToken, oldRefreshToken)
    }
}