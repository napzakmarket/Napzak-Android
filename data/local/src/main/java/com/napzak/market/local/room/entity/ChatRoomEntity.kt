package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "chat_room")
data class ChatRoomEntity(
    @PrimaryKey val roomId: Long,
    val productId: Long? = null,
    val opponentNickName: String,
    val opponentStorePhoto: String,
    val lastMessage: String = "",
    val lastMessageAt: String = "",
    val unreadCount: Int = 0,
    val isOpponentOnline: Boolean = false,
    val isWithdrawn: Boolean = false,
    val isReported: Boolean = false,
    val isOpponentStoreBlocked: Boolean = false,
    val isChatBlocked: Boolean = false,
    val lastUpdated: String = LocalDate.now().toString()
)
