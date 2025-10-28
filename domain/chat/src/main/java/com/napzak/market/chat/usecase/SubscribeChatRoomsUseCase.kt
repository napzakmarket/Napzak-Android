package com.napzak.market.chat.usecase

import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class SubscribeChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatSocketRepository: ChatSocketRepository
) {
    suspend operator fun invoke(storeId: Long): Result<Unit> {
        return chatRepository.getChatRoomIds().mapCatching { roomIds ->
            roomIds.forEach { roomId ->
                chatSocketRepository.subscribeChatRoom(roomId)
            }
        }
    }
}
