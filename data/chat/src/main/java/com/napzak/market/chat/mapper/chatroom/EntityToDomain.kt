package com.napzak.market.chat.mapper.chatroom

import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.local.room.entity.ChatRoomEntity

internal fun ChatRoomEntity.toDomain() = ChatRoom(
    roomId = roomId,
    storeNickname = opponentNickName,
    storePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    unreadMessageCount = unreadCount,
    lastMessageAt = lastMessageAt,
    isOpponentWithdrawn = isWithdrawn,
)
