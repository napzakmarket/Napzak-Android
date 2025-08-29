package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import com.napzak.market.chat.repository.ChatRepository
import javax.inject.Inject

class SubscribeChatRoomsUseCase @Inject constructor(
    private val chatController: ChatController,
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(storeId: Long): Result<Unit> {
        return runCatching {
            chatRepository.getChatRoomIds().onSuccess { roomIds ->
                roomIds.forEach { roomId ->
                    chatController.subscribeChatRoom(roomId, storeId)
                }
            }
        }
    }
}
