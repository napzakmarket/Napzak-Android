package com.napzak.market.chat.chatroom.model

import com.napzak.market.chat.chatroom.preview.mockProduct
import com.napzak.market.product.model.Product

// TODO: 임시 모델
data class ChatRoom(
    val chatRoomId: Long,
    val storeName: String,
    val product: Product,
) {
    companion object {
        val mock = ChatRoom(
            chatRoomId = 1,
            storeName = "마이린",
            product = mockProduct,
        )
    }
}