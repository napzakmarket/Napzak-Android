package com.napzak.market.chat.chatroom.preview

import com.napzak.market.chat.chatroom.ChatRoomState
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.StoreBrief

internal val mockChats
    get() =
        listOf<ReceiveMessage<*>>(
            ReceiveMessage.Notice(
                roomId = 1,
                messageId = 14,
                notice = "채팅방을 나갔습니다",
                timeStamp = "오후 9:01",
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 13,
                text = "응 너랑 거래 안해~.",
                timeStamp = "오후 9:00",
                isRead = false,
                isMessageOwner = false,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 12,
                text = "한 번만 봐주세요.",
                timeStamp = "오후 8:00",
                isRead = true,
                isMessageOwner = false,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 11,
                text = "신고합니다.",
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = true,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 10,
                text = "좀 애매하긴 해",
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = false,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 9,
                text = "구매 할까말까 할까말까",
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = false,
            ),
            ReceiveMessage.Date(
                roomId = 1,
                messageId = 8,
                date = "2025년 4월 30일",
                timeStamp = "오후 7:30",
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 7,
                text = "사죄드립니다.",
                timeStamp = "오후 7:40",
                isRead = true,
                isMessageOwner = false
            ),
            ReceiveMessage.Image(
                roomId = 1,
                messageId = 6,
                imageUrl = "",
                timeStamp = "오후 7:37",
                isRead = true,
                isMessageOwner = true
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 5,
                text = "뭐야 가세요",
                timeStamp = "오후 7:35",
                isRead = true,
                isMessageOwner = true
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 4,
                text = "?",
                timeStamp = "오후 7:35",
                isRead = true,
                isMessageOwner = true,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 3,
                text = "좀 애매하긴 해",
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = false,
            ),
            ReceiveMessage.Text(
                roomId = 1,
                messageId = 2,
                text = "구매 할까말까 할까말까",
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = false,
            ),
            ReceiveMessage.Product(
                product = mockProductBrief,
                roomId = 1,
                messageId = 1,
                timeStamp = "오후 7:30",
                isRead = true,
                isMessageOwner = false,
            )
        )

internal val mockProductBrief
    get() = ProductBrief(
        productId = 1,
        genreName = "은혼",
        title = "은혼 긴토키 히지카타 룩업",
        photo = "",
        price = 129000,
        tradeType = "SELL",
        isPriceNegotiable = false,
        isMyProduct = false,
        productOwnerId = 1
    )

internal val mockChatRoom = ChatRoomState(
    roomId = 1,
    productBrief = ProductBrief(
        productId = 1,
        photo = "",
        tradeType = "",
        title = "",
        price = 10000,
        genreName = "",
        isPriceNegotiable = true,
        productOwnerId = 1,
        isMyProduct = true
    ),
    storeBrief = StoreBrief(
        storeId = 1,
        nickname = "",
        storePhoto = "",
        isWithdrawn = true,
        isReported = true,
    )
)