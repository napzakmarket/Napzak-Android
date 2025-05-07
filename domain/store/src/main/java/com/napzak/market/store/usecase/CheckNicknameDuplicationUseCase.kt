package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository

class CheckNicknameDuplicationUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Boolean> {
        return storeRepository.getValidateNickname(nickname)
            .mapCatching { true }
    }
}