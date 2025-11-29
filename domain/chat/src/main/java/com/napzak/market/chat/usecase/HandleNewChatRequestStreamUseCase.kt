package com.napzak.market.chat.usecase

import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class HandleNewChatRequestStreamUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() {
        chatSocketRepository.getNewChatRequestStream()
            .collect { roomId ->
                chatSocketRepository.subscribeChatRoom(roomId)
                chatRepository.fetchChatRoomsFromRemote()
            }
    }
}