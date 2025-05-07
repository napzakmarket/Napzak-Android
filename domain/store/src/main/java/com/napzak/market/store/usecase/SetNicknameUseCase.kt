package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository

class SetNicknameUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Unit> {
        return runCatching {
            storeRepository.postRegisterNickname(nickname)
        }
    }
}