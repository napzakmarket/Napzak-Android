package com.napzak.market.local.dummy

import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatRoomEntity

object DummyChatRoomFactory {
    fun createEntity(
        roomId: Long,
        productId: Long = roomId * 10,
        opponentStoreId: Long = roomId * 100,
        opponentNickName: String = "nickname-$roomId",
        opponentStorePhoto: String = "photo-$roomId",
        isWithdrawn: Boolean = false,
        isReported: Boolean = false,
        isOpponentStoreBlocked: Boolean = false,
        isChatBlocked: Boolean = false,
    ) = ChatRoomEntity(
        roomId = roomId,
        productId = productId,
        opponentStoreId = opponentStoreId,
        opponentNickName = opponentNickName,
        opponentStorePhoto = opponentStorePhoto,
        isWithdrawn = isWithdrawn,
        isReported = isReported,
        isOpponentStoreBlocked = isOpponentStoreBlocked,
        isChatBlocked = isChatBlocked,
    )

    fun createEntities(count: Int): List<ChatMessageEntity> {
        return (1L..count).map { DummyChatMessageEntityFactory.createEntity(it) }
    }
}