package com.napzak.market.chat.chatroom.preview

import com.napzak.market.chat.chatroom.model.ChatDirection
import com.napzak.market.chat.chatroom.model.ChatItem
import com.napzak.market.product.model.Product

internal val mockChats
    get() =
    listOf<ChatItem<*>>(
        ChatItem.Notice(
            notice = "채팅방을 나갔습니다",
            timeStamp = "오후 9:01",
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "응 너랑 거래 안해~.",
            timeStamp = "오후 9:00",
            isRead = false,
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "한 번만 봐주세요.",
            timeStamp = "오후 8:00",
            isRead = false,
        ),
        ChatItem.Text(
            direction = ChatDirection.SENT,
            text = "신고합니다.",
            timeStamp = "오후 7:30",
            isRead = false,
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "좀 애매하긴 해",
            timeStamp = "오후 7:30",
            isRead = true,
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "구매 할까말까 할까말까",
            timeStamp = "오후 7:30",
            isRead = false,
        ),
        ChatItem.Date(
            date = "2025년 4월 30일",
            timeStamp = "오후 7:30",
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "사죄드립니다.",
            timeStamp = "오후 7:40",
            isRead = true,
        ),
        ChatItem.Image(
            direction = ChatDirection.SENT,
            imageUrl = "",
            timeStamp = "오후 7:37",
            isRead = true,
        ),
        ChatItem.Text(
            direction = ChatDirection.SENT,
            text = "뭐야 가세요",
            timeStamp = "오후 7:35",
            isRead = true,
        ),
        ChatItem.Text(
            direction = ChatDirection.SENT,
            text = "?",
            timeStamp = "오후 7:35",
            isRead = true,
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "좀 애매하긴 해",
            timeStamp = "오후 7:30",
            isRead = true,
        ),
        ChatItem.Text(
            direction = ChatDirection.RECEIVED,
            text = "구매 할까말까 할까말까",
            timeStamp = "오후 7:30",
            isRead = true,
        ),
        ChatItem.Product(
            direction = ChatDirection.RECEIVED,
            product = mockProduct,
            timeStamp = "오후 7:30",
            isRead = true,
        )
    )

val mockProduct
    get() = Product(
        productId = 1,
        genreName = "은혼",
        productName = "은혼 긴토키 히지카타 룩업",
        photo = "",
        price = 129000,
        uploadTime = "",
        isInterested = true,
        tradeType = "SELL",
        tradeStatus = "RESERVED",
        isPriceNegotiable = false,
        isOwnedByCurrentUser = false,
        interestCount = 10,
        chatCount = 10,
    )