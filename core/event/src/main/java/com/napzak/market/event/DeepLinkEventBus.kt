package com.napzak.market.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<DeepLinkEvent>()
    val events = _events.asSharedFlow()

    suspend fun send(event: DeepLinkEvent) {
        _events.emit(event)
    }
}

sealed interface DeepLinkEvent {
    data class NavigateToChatRoom(val chatRoomId: Long) : DeepLinkEvent
    data class NavigateToProductDetail(val productId: Long) : DeepLinkEvent
}
