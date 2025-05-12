package com.napzak.market.store.usecase

import com.napzak.market.store.model.KakaoLogin
import com.napzak.market.store.repository.AuthRepository
import javax.inject.Inject

class SetKakaoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(kakaoToken: String): Result<KakaoLogin> {
        return authRepository.loginWithKakao(kakaoToken)
    }
}