package com.napzak.market.chat.usecase

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

/**
 * 새로운 채팅방을 생성하고, 입장 및 구독을 시작합니다.
 *
 * 새 채팅방 아이디 `roomId: Long`을 반환합니다.
 */
class CreateChatRoomUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(
        productId: Long,
        receiverStoreId: Long,
    ): Result<Long> = runCatching {
        val roomId = getNewRoomId(productId, receiverStoreId)
        val chatRoomInformation = getChatRoomInformation(productId, roomId)
        val productBrief = chatRoomInformation.productBrief

        enterChatRoom(roomId)
        sendProductMessage(roomId, productBrief)
        roomId
    }

    private suspend fun getNewRoomId(productId: Long, receiverStoreId: Long): Long =
        chatRoomRepository.createChatRoom(productId, receiverStoreId)
            .getOrThrow()

    private suspend fun enterChatRoom(roomId: Long) {
        chatRoomRepository.enterChatRoom(roomId)
        chatSocketRepository.subscribeChatRoom(roomId)
    }

    private suspend fun getChatRoomInformation(productId: Long, roomId: Long) =
        chatRoomRepository.getChatRoomInformation(productId, roomId).getOrThrow()

    private suspend fun sendProductMessage(roomId: Long, productBrief: ProductBrief) {
        val message = SendMessage.Product(roomId, null, productBrief)
        chatSocketRepository.sendChat(message)
    }
}