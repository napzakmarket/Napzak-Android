package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import com.napzak.market.chat.model.ReceiveMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class GetChatFlowUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    operator fun invoke(roomId: Long): Flow<ReceiveMessage<*>> {
        return chatController.messageFlow.filter { it.roomId == roomId }
    }
}
