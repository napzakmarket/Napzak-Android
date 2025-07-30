package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomListResponse(
    @SerialName("chatRooms") val chatRooms: List<ChatRoom>,
    @SerialName("currentStoreId") val myStoreId: Long,
    @SerialName("isMessageAllowed") val isMessageAllowed: Boolean? = null,
) {
    @Serializable
    data class ChatRoom(
        @SerialName("roomId") val roomId: Long,
        @SerialName("opponentNickname") val opponentNickname: String,
        @SerialName("isOpponentWithdrawn") val isOpponentWithdrawn: Boolean,
        @SerialName("lastMessage") val lastMessage: String,
        @SerialName("lastMessageAt") val lastMessageAt: String,
        @SerialName("unreadCount") val unreadCount: Int,
        @SerialName("opponentStorePhoto") val opponentStorePhoto: String,
    )
}
