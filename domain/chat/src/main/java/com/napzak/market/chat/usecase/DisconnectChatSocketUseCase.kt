package com.napzak.market.chat.usecase

import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class DisconnectChatSocketUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return chatSocketRepository.disconnect()
    }
}