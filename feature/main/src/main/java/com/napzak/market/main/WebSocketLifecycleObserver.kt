package com.napzak.market.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.util.android.TokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebSocketLifecycleObserver @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
    private val chatRepository: ChatRepository,
    private val tokenProvider: TokenProvider,
) : DefaultLifecycleObserver {
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val isLoggedIn = MutableStateFlow(false)

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        activityScope.launch {
            isLoggedIn.collectLatest { loggedIn ->
                if (loggedIn && isTokenAvailable()) {
                    chatSocketRepository.connect()
                        .onSuccess {
                            val roomIds = chatRepository.getChatRoomIds().getOrElse { emptyList() }
                            subscribeChatRooms(roomIds)
                        }
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        activityScope.launch { chatSocketRepository.disconnect() }
    }

    fun updateLoggedInState(isLoggedIn: Boolean) {
        this.isLoggedIn.update { isLoggedIn }
    }

    private fun subscribeChatRooms(roomIds: List<Long>) {
        activityScope.launch {
            roomIds.map { roomId ->
                async {
                    chatSocketRepository.subscribeChatRoom(roomId)
                }
            }.awaitAll()
        }
    }

    private suspend fun isTokenAvailable() = tokenProvider.getAccessToken() != null
}