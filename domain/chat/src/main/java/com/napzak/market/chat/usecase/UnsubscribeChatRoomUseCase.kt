package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import javax.inject.Inject

class UnsubscribeChatRoomUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    operator fun invoke(roomId: Long) {
        chatController.unsubscribeChatRoom(roomId)
    }
}
