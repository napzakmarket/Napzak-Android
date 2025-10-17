package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatStatusType

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uuid: String? = null,
    val messageId: Long? = null,
    val roomId: Long? = null,
    val senderId: Long? = null,
    val messageType: ChatMessageType,
    val message: String? = null,
    val createdAt: String,
    val isMessageOwner: Boolean = false,
    val isProfileNeeded: Boolean = false,
    val isRead: Boolean = false,
    val status: ChatStatusType = ChatStatusType.PENDING,
)
