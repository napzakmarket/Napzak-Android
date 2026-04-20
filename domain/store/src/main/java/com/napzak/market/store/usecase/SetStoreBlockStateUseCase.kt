package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class SetStoreBlockStateUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(storeId: Long, targetState: Boolean) = runCatching {
        when (targetState) {
            true -> storeRepository.blockStore(storeId)
            false -> storeRepository.unblockStore(storeId)
        }.getOrElse {
            throw Throwable(cause = it, message = "스토어 차단 상태 변경 실패")
        }
    }
}