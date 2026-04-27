package com.napzak.market.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.napzak.market.chat.usecase.ConnectChatSocketUseCase
import com.napzak.market.chat.usecase.DisconnectChatSocketUseCase
import com.napzak.market.chat.usecase.SubscribeChatRoomsUseCase
import com.napzak.market.event.SocketEvent
import com.napzak.market.event.SocketEventBus
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.TokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebSocketLifecycleObserver @Inject constructor(
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val socketEventBus: SocketEventBus,
    private val connectChatSocket: ConnectChatSocketUseCase,
    private val disconnectChatSocketUseCase: DisconnectChatSocketUseCase,
    private val subscribeChatRooms: SubscribeChatRoomsUseCase,
) : DefaultLifecycleObserver {
    private lateinit var activityScope: CoroutineScope
    private var socketConnectJob: Job? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        socketConnectJob = activityScope.launch {
            socketEventBus.connectionState.collect { event ->
                when (event) {
                    is SocketEvent.Connect -> performConnect()
                    is SocketEvent.Disconnect -> disconnectChatSocketUseCase()
                }
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        socketConnectJob?.cancel()
        activityScope.launch { disconnectChatSocketUseCase() }
        super.onPause(owner)
    }

    private suspend fun performConnect() {
        // 토큰 존재 여부만 확인합니다.
        val token = tokenProvider.getAccessToken() ?: return
        val storeId = fetchStoreInfo()?.storeId ?: return

        runCatching {
            connectChatSocket(storeId)
            subscribeChatRooms(storeId)
        }
    }

    private suspend fun fetchStoreInfo(): StoreInfo? {
        return storeRepository.fetchStoreInfo().getOrNull()
    }
}
