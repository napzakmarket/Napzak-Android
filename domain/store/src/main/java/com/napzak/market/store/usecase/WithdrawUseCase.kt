package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.StoreStateManager
import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val userStateManager: StoreStateManager,
) {
    suspend operator fun invoke(
        title: String,
        description: String? = null
    ): Result<Unit> = runCatching {
        userStateManager.setIsDeleting(true)

        storeRepository.withdraw(title, description)
            .getOrThrow()

        tokenProvider.clearTokens()
    }
}