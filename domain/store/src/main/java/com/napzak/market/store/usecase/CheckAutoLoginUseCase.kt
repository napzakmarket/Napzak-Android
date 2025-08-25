package com.napzak.market.store.usecase

import com.napzak.market.store.repository.AuthRepository
import com.napzak.market.util.android.TokenProvider
import javax.inject.Inject

class CheckAutoLoginUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val access = tokenProvider.getAccessToken()
        require(!access.isNullOrBlank()) { "No access token" }

        val role = tokenProvider.getAccessTokenRole()
            ?.removePrefix("ROLE_")
            ?.uppercase()

        if (role == "STORE") {
            Unit
        } else {
            tokenProvider.clearTokens()
            error("Not active role: $role")
        }
    }
}