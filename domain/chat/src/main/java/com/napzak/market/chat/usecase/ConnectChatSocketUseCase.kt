package com.napzak.market.chat.usecase

import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class ConnectChatSocketUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(storeId: Long): Result<Unit> = runCatching {
        chatSocketRepository.connect().getOrThrow()
        chatSocketRepository.subscribeCreateChatRoom(storeId).getOrThrow()
    }
}