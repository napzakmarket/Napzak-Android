package com.napzak.market.chat.paging

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatRoomMessagesResponse
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.remote.model.BaseResponse

object ChatRoomMessageTestUtil {
    fun mockResponse(
        messages: List<MessageItem> = mockMessages(),
        cursor: String? = null,
    ) = BaseResponse(
        status = 200,
        message = "success",
        data = ChatRoomMessagesResponse(
            messages = messages,
            cursor = cursor,
        ),
    )

    fun mockMessages(): List<MessageItem> = listOf(
        MessageItem(
            messageId = 29,
            senderId = 3,
            type = "IMAGE",
            content = "",
            metadata = ChatMessageMetadata.Image(
                imageUrls = listOf(
                    "https://cdn.myapp.com/images/123.jpg",
                )
            ),
            createdAt = "오전 9:04",
            isRead = true,
            isMessageOwner = false,
            isProfileNeeded = true
        ),

        MessageItem(
            messageId = 28,
            senderId = 3,
            type = "PRODUCT",
            content = "",
            metadata = ChatMessageMetadata.Product(
                tradeType = "BUY",
                productId = 9876,
                genreName = "은혼",
                title = "은혼 긴토키 히지카타 특업",
                price = 125000,
                isProductDeleted = false
            ),
            createdAt = "오전 9:01",
            isRead = true,
            isMessageOwner = false,
            isProfileNeeded = false
        ),

        MessageItem(
            messageId = 27,
            senderId = 1,
            type = "TEXT",
            content = "얏호바뤼",
            metadata = null,
            createdAt = "오전 9:01",
            isRead = false,
            isMessageOwner = true,
            isProfileNeeded = false
        ),

        MessageItem(
            messageId = 26,
            senderId = 1,
            type = "TEXT",
            content = "제발",
            metadata = null,
            createdAt = "오전 9:00",
            isRead = false,
            isMessageOwner = true,
            isProfileNeeded = false
        ),

        MessageItem(
            messageId = 25,
            senderId = null,
            type = "DATE",
            content = null,
            metadata = ChatMessageMetadata.Date(
                date = "2025년 7월 13일"
            ),
            createdAt = "오전 9:00",
            isRead = true,
            isMessageOwner = false,
            isProfileNeeded = false
        )
    )
}