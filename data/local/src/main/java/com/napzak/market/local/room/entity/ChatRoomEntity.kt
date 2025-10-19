package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_room")
data class ChatRoomEntity(
    @PrimaryKey val roomId: Long,
    val productId: Long,
    val opponentStoreId: Long,
    val opponentNickName: String,
    val opponentStorePhoto: String,
    val isWithdrawn: Boolean,
    val isReported: Boolean,
    val isOpponentStoreBlocked: Boolean,
    val isChatBlocked: Boolean,
)
