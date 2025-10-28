package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatStatusType

@Entity(
    tableName = "chat_messages",
    indices = [Index("roomId")],
)
data class ChatMessageEntity(
    @PrimaryKey val messageId: Long,
    val uuid: String? = null,
    val roomId: Long? = null,
    val senderId: Long? = null,
    val messageType: ChatMessageType,
    val message: String? = null,
    val productId: Long? = null,
    val createdAt: String,
    val isMessageOwner: Boolean = false,
    val isProfileNeeded: Boolean = false,
    val isRead: Boolean = false,
    val status: ChatStatusType = ChatStatusType.PENDING,
)
