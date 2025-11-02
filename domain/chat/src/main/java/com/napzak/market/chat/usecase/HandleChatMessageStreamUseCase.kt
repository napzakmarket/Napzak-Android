package com.napzak.market.chat.usecase

import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HandleChatMessageStreamUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
    private val chatRoomRepository: ChatRoomRepository,
) {
    suspend operator fun invoke(storeId: Long): Flow<Result<Unit>> {
        return chatSocketRepository.getChatMessageStream(storeId).map { message ->
            runCatching {
                when (message) {
                    is ReceiveMessage.Join,
                    is ReceiveMessage.Leave -> {
                        setOpponentOnlineStatus(storeId, message)
                    }

                    else -> {
                        insertMessageInLocal(message)
                        updateChatRoom(message)
                    }
                }
            }
        }
    }

    private suspend fun setOpponentOnlineStatus(
        myStoreId: Long,
        message: ReceiveMessage<*>,
    ) {
        val roomId = message.roomId
        if (myStoreId != message.senderId) {
            val isOpponentOnline = message is ReceiveMessage.Join
            chatRoomRepository.setOpponentOnlineStatusInLocal(roomId, isOpponentOnline)
        }
    }

    private suspend fun insertMessageInLocal(message: ReceiveMessage<*>) {
        val isRead = checkIfMessageIsRead(message)
        chatRoomRepository.insertMessageIntoLocal(message, isRead)
    }

    private suspend fun checkIfMessageIsRead(message: ReceiveMessage<*>): Boolean {
        val roomId = message.roomId
        val chatRoom = chatRoomRepository.getChatRoomInformationFromLocal(roomId).getOrThrow()
        return chatRoom?.isOpponentOnline == true || !message.isMessageOwner
    }

    private suspend fun updateChatRoom(message: ReceiveMessage<*>) {
        chatRoomRepository.setChatRoomByMessageInLocal(message)
    }
}