package com.napzak.market.local.dummy

import com.napzak.market.local.room.entity.ChatRoomEntity

object DummyChatRoomFactory {
    fun createEntity(
        roomId: Long,
        productId: Long = roomId * 10,
        opponentNickName: String = "nickname-$roomId",
        opponentStorePhoto: String = "photo-$roomId",
        isWithdrawn: Boolean = false,
        isReported: Boolean = false,
        isOpponentStoreBlocked: Boolean = false,
        isChatBlocked: Boolean = false,
        isOpponentOnline: Boolean = false,
        unreadCount: Int = 0,
    ) = ChatRoomEntity(
        roomId = roomId,
        productId = productId,
        opponentNickName = opponentNickName,
        opponentStorePhoto = opponentStorePhoto,
        isWithdrawn = isWithdrawn,
        isReported = isReported,
        isOpponentStoreBlocked = isOpponentStoreBlocked,
        isChatBlocked = isChatBlocked,
        isOpponentOnline = isOpponentOnline,
        unreadCount = unreadCount,
    )

    fun createEntities(count: Int): List<ChatRoomEntity> {
        return (1..count).map { createEntity(it.toLong()) }
    }
}