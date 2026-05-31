package com.napzak.market.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 알림 클릭 시 목적지를 전역적으로 공유하기 위한 EventBus
 */
@Singleton
class ChatSessionManager @Inject constructor() {
    private val _chatRoomId = MutableStateFlow<Long?>(null)
    val chatRoomId = _chatRoomId.asStateFlow()

    fun setChatRoomId(chatRoomId: Long?) {
        _chatRoomId.update { chatRoomId }
    }
}
