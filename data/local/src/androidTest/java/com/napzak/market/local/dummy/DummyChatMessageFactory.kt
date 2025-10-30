package com.napzak.market.local.dummy

import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatStatusType

object DummyChatMessageEntityFactory {
    fun createEntity(
        id: Long,
        uuid: String = "uuid-dummy-$id",
        messageId: Long = id * 100,
        roomId: Long = id,
        senderId: Long = id + 1,
        message: String = "hello world",
        messageType: ChatMessageType = ChatMessageType.TEXT,
        createdAt: String = "00:00",
        isMessageOwner: Boolean = false,
        isProfileNeeded: Boolean = false,
        isRead: Boolean = false,
        status: ChatStatusType = ChatStatusType.PENDING
    ) = ChatMessageEntity(
        messageId = messageId,
        uuid = uuid,
        roomId = roomId,
        senderId = senderId,
        message = message,
        messageType = messageType,
        createdAt = createdAt,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
        isRead = isRead,
        status = status
    )

    fun createEntities(count: Int): List<ChatMessageEntity> {
        return (1..count).map { createEntity(it.toLong()) }
    }
}