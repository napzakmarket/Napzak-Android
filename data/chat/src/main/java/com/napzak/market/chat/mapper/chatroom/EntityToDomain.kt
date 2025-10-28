package com.napzak.market.chat.mapper.chatroom

import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.StoreBrief
import com.napzak.market.local.room.entity.ChatRoomEntity
import com.napzak.market.local.room.relation.ChatRoomWithProduct

internal fun ChatRoomEntity.toDomain() = ChatRoom(
    roomId = roomId,
    storeNickname = opponentNickName,
    storePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    unreadMessageCount = unreadCount,
    lastMessageAt = lastMessageAt,
    isOpponentWithdrawn = isWithdrawn,
)

internal fun ChatRoomWithProduct.toDomain(): ChatRoomInformation? {
    val productBrief = product?.toDomain() ?: return null
    return ChatRoomInformation(
        roomId = room.roomId,
        storeBrief = room.toStoreBrief(),
        productBrief = productBrief
    )
}

private fun ChatRoomEntity.toStoreBrief() = StoreBrief(
    storeId = 0,
    nickname = opponentNickName,
    storePhoto = opponentStorePhoto,
    isWithdrawn = isWithdrawn,
    isReported = isReported,
    isOpponentStoreBlocked = isOpponentStoreBlocked,
    isMyStoreBlocked = isChatBlocked,
)