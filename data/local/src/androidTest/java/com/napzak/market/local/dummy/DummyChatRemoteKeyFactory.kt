package com.napzak.market.local.dummy

import com.napzak.market.local.room.entity.ChatRemoteKeyEntity

object DummyChatRemoteKeyFactory {
    fun createEntity(
        id: Long,
        refreshAnchorMessageId: Long = id * 100,
        appendCursor: String? = id.toString(),
    ) = ChatRemoteKeyEntity(
        roomId = id,
        refreshAnchorMessageId = refreshAnchorMessageId,
        appendCursor = appendCursor
    )

    fun createEntities(count: Int): List<ChatRemoteKeyEntity> {
        return (1L..count).map { createEntity(it) }
    }
}