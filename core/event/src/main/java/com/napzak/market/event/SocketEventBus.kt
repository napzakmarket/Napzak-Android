package com.napzak.market.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 앱을 실행했을 때 실제로 로그인이 완료되었는지를 전역적으로 공유하기 위한 EventBus
 */
@Singleton
class SocketEventBus @Inject constructor() {
    private val _connectionState = MutableStateFlow<SocketEvent>(SocketEvent.Disconnect)
    val connectionState: StateFlow<SocketEvent> = _connectionState.asStateFlow()

    fun connect() {
        _connectionState.update { SocketEvent.Connect }
    }

    fun disconnect() {
        _connectionState.update { SocketEvent.Disconnect }
    }
}

sealed interface SocketEvent {
    data object Connect : SocketEvent
    data object Disconnect : SocketEvent
}
