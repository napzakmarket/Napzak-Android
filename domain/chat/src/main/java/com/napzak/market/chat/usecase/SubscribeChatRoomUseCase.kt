package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import javax.inject.Inject

class SubscribeChatRoomUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    suspend operator fun invoke(roomId: Long, storeId: Long): Result<Unit> {
        return chatController.subscribeChatRoom(roomId, storeId)
    }
}
