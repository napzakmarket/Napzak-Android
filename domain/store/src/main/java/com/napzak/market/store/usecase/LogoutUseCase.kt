package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.StoreStateManager
import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val storeStateManager: StoreStateManager,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        storeRepository.logout()
        storeStateManager.setIsDeleting(false)
        tokenProvider.updateAccessToken(null)
    }
}