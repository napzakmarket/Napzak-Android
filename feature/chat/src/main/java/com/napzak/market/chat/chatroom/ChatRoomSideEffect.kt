package com.napzak.market.chat.chatroom

sealed interface ChatRoomSideEffect {
    data object OnSendChatMessage : ChatRoomSideEffect
    data object OnReceiveChatMessage : ChatRoomSideEffect
    data object OnWithdrawChatRoom : ChatRoomSideEffect
    data class ShowToast(val message: String) : ChatRoomSideEffect
    data object OnErrorOccurred : ChatRoomSideEffect
    data class OnChangeBlockState(val newState: Boolean) : ChatRoomSideEffect
}
