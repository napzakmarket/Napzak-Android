package com.napzak.market.chat.model

data class ChatRoom(
    val roomId: Long,
    val storeNickname: String,
    val storePhoto: String,
    val lastMessage: String,
    val unreadMessageCount: Int,
    val lastMessageAt: String,
)
