package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import com.napzak.market.chat.model.ReceiveMessage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetAllChatFlowsUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    operator fun invoke(): SharedFlow<ReceiveMessage<*>> {
        return chatController.messageFlow
    }
}