package com.napzak.market.chat.chatlist.model

import com.napzak.market.product.model.Product

data class ChatRoomDetail(
    val chatRoomId: Long,
    val storeName: String,
    val storeImage: String,
    val lastMessage: String,
    val unReadMessageCount: Int,
    val timeStamp: String,
    val product: Product?,
) {
    companion object {
        val mockList = buildList {
            repeat(20) { index ->
                val randomHour = (0..12).random().toString()
                val randomMinute = (0..60).random().toString().padStart(2, '0')
                val randomCount = (0..1000).random()

                add(
                    ChatRoomDetail(
                        chatRoomId = index.toLong(),
                        storeName = "납자기$index",
                        storeImage = "",
                        lastMessage = "${index}번째로 메세지를 보냄 ",
                        unReadMessageCount = randomCount,
                        timeStamp = "오후 $randomHour:$randomMinute",
                        product = null,
                    )
                )
            }
        }
    }
}
