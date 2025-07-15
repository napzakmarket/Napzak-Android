package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatRoomListRequest
import com.napzak.market.chat.model.ChatRoom

fun ChatRoomListRequest.ChatRoom.toDomain() = ChatRoom(
    roomId = roomId,
    storeNickname = opponentNickname,
    storePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    unreadMessageCount = unreadCount,
    lastMessageAt = lastMessageAt,
)
