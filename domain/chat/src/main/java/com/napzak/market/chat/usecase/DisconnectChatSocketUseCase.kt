package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import javax.inject.Inject

class DisconnectChatSocketUseCase @Inject constructor(
    private val chatController: ChatController
) {
    suspend operator fun invoke(): Result<Unit> {
        return chatController.disconnect()
    }
}