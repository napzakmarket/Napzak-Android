package com.napzak.market.chat.chatroom

sealed interface ChatRoomSideEffect {
    data object OnWithDrawChatRoom : ChatRoomSideEffect
}