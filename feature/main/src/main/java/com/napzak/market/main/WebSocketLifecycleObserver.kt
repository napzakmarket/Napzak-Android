package com.napzak.market.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.usecase.ConnectChatSocketUseCase
import com.napzak.market.chat.usecase.DisconnectChatSocketUseCase
import com.napzak.market.chat.usecase.SubscribeChatRoomUseCase
import com.napzak.market.store.repository.StoreRepository
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
    private val chatRepository: ChatRepository,
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val connectChatSocketUseCase: ConnectChatSocketUseCase,
    private val disconnectChatSocketUseCase: DisconnectChatSocketUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,
) : DefaultLifecycleObserver {
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val isLoggedIn = MutableStateFlow(false)

    fun updateLoggedInState(isLoggedIn: Boolean) {
        this.isLoggedIn.update { isLoggedIn }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        activityScope.launch {
            isLoggedIn.collectLatest { loggedIn ->
                if (loggedIn && isTokenAvailable()) {
                    storeRepository.fetchStoreInfo().onSuccess { storeInfo ->
                        connectChatSocketUseCase(storeInfo.storeId)
                        subscribeChatRooms(storeInfo.storeId)
                    }
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        activityScope.launch { disconnectChatSocketUseCase() }
    }

    private suspend fun subscribeChatRooms(storeId: Long) {
        chatRepository.getChatRoomIds().onSuccess { roomIds ->
            activityScope.launch {
                roomIds.map { roomId ->
                    async {
                        subscribeChatRoomUseCase(roomId = roomId, storeId = storeId)
                    }
                }.awaitAll()
            }
        }
    }

    private suspend fun isTokenAvailable() = tokenProvider.getAccessToken() != null
}