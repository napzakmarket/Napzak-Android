package com.napzak.market.main

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ChatDeepLinkEventBus {
    private val _events = MutableSharedFlow<ChatDeepLinkEvent>()
    val events = _events.asSharedFlow()

    suspend fun send(event: ChatDeepLinkEvent) {
        _events.emit(event)
    }
}

sealed interface ChatDeepLinkEvent {
    data class ChatRoom(val chatRoomId: String?) : ChatDeepLinkEvent
}
