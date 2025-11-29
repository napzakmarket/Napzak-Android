package com.napzak.market.chat.usecase

import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.type.ChatCondition
import javax.inject.Inject

class GetChatRoomInformationUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(
        roomId: Long?,
        productId: Long?,
    ) = runCatching {
        when {
            roomId != null -> fetchChatRoomInformationByRoomId(roomId)
            productId != null -> fetchChatRoomInformationByProductId(productId)
            else -> throw Throwable(message = ERROR_MESSAGE)
        }
    }

    private suspend fun fetchChatRoomInformationByRoomId(
        roomId: Long,
    ): Pair<ChatRoomInformation, ChatCondition> {
        val productId = enterChatRoom(roomId)
        val info = chatRoomRepository.getChatRoomInformation(productId, roomId).getOrThrow()
        return info to ChatCondition.PRODUCT_NOT_CHANGED
    }

    private suspend fun fetchChatRoomInformationByProductId(
        productId: Long,
    ): Pair<ChatRoomInformation, ChatCondition> {
        val info = chatRoomRepository.getChatRoomInformation(productId, null).getOrThrow()
        val roomId = info.roomId
        val chatCondition = if (roomId == null) {
            ChatCondition.NEW_CHAT_ROOM
        } else {
            val remoteProductId = enterChatRoom(roomId)
            if (remoteProductId != productId) {
                ChatCondition.PRODUCT_CHANGED
            } else {
                ChatCondition.PRODUCT_NOT_CHANGED
            }
        }
        return info to chatCondition
    }

    private suspend fun enterChatRoom(roomId: Long) =
        chatRoomRepository.enterChatRoom(roomId).getOrThrow()

    companion object {
        private const val ERROR_MESSAGE = "채팅방 정보를 불러올 수 없습니다."
    }
}