package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import com.napzak.market.chat.model.SendMessage
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    suspend operator fun invoke(message: SendMessage<*>) {
        chatController.sendMessage(message)
    }
}
