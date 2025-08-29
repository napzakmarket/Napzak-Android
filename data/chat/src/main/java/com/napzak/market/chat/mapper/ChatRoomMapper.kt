package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatRoomListResponse
import com.napzak.market.chat.model.ChatRoom

fun ChatRoomListResponse.ChatRoom.toDomain() = ChatRoom(
    roomId = roomId,
    storeNickname = opponentNickname,
    storePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    unreadMessageCount = unreadCount,
    lastMessageAt = lastMessageAt,
    isOpponentWithdrawn = isOpponentWithdrawn,
)
