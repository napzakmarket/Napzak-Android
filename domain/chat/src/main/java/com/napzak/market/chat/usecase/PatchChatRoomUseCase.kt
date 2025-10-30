package com.napzak.market.chat.usecase

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import javax.inject.Inject

class PatchChatRoomUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(
        roomId: Long,
        productBrief: ProductBrief,
    ) = runCatching {
        patchChatRoomProduct(roomId, productBrief)
        sendProductMessage(roomId, productBrief)
    }

    private suspend fun patchChatRoomProduct(roomId: Long, productBrief: ProductBrief) {
        val productId = productBrief.productId
        chatRoomRepository.patchChatRoomProduct(roomId, productId).getOrThrow()
    }

    private suspend fun sendProductMessage(roomId: Long, productBrief: ProductBrief) {
        val message = SendMessage.Product(roomId, null, productBrief)
        chatSocketRepository.sendChat(message).getOrThrow()
    }
}