package com.napzak.market.chat.usecase

import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(message: SendMessage<*>) =
        chatSocketRepository.sendChat(message)
}