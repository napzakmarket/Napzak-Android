package com.napzak.market.chat.chatroom

sealed interface ChatRoomSideEffect {
    data object OnSendChatMessage : ChatRoomSideEffect
    data object OnReceiveChatMessage : ChatRoomSideEffect
    data object OnWithdrawChatRoom : ChatRoomSideEffect
}