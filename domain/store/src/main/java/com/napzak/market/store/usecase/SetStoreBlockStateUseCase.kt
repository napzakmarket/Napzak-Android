package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class SetStoreBlockStateUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(targetState: Boolean) = runCatching {
        val storeId = getStoreId()
        setBlockState(storeId, targetState)
    }

    private suspend fun getStoreId(): Long {
        return storeRepository.fetchStoreInfo()
            .getOrElse { t ->
                throw Throwable(cause = t, message = "스토어 정보 조회 실패")
            }.storeId
    }

    private suspend fun setBlockState(storeId: Long, targetState: Boolean) {
        when (targetState) {
            true -> storeRepository.blockStore(storeId)
            false -> storeRepository.unblockStore(storeId)
        }.getOrElse {
            throw Throwable(cause = it, message = "스토어 차단 상태 변경 실패")
        }
    }
}