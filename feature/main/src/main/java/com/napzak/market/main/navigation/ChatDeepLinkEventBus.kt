package com.napzak.market.main.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Deprecated("현재 알림 처리 로직에는 사용하지 않습니다")
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
