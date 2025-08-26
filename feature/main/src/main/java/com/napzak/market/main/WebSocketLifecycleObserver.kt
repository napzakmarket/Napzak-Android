package com.napzak.market.main

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.usecase.ConnectChatSocketUseCase
import com.napzak.market.chat.usecase.DisconnectChatSocketUseCase
import com.napzak.market.chat.usecase.SubscribeChatRoomUseCase
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.TokenProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebSocketLifecycleObserver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRepository: ChatRepository,
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val connectChatSocketUseCase: ConnectChatSocketUseCase,
    private val disconnectChatSocketUseCase: DisconnectChatSocketUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,
) : DefaultLifecycleObserver {
    private lateinit var activityScope: CoroutineScope
    private var loginStateCollectJob: Job? = null
    private val isLoggedIn = MutableStateFlow(false)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        loginStateCollectJob = activityScope.launch {
            isLoggedIn.collectLatest { isLoggedIn ->
                if (isLoggedIn && isTokenAvailable()) {
                    val storeId = fetchStoreInfo()?.storeId ?: return@collectLatest
                    runCatching {
                        connectChatSocket(storeId)
                        subscribeChatRooms(storeId)
                    }
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        loginStateCollectJob?.cancel()
        activityScope.launch { disconnectChatSocketUseCase() }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        activityScope.cancel()
    }

    fun updateLoggedInState(isLoggedIn: Boolean) {
        this.isLoggedIn.update { isLoggedIn }
    }

    private suspend fun fetchStoreInfo(): StoreInfo? = storeRepository.fetchStoreInfo().getOrNull()

    private suspend fun connectChatSocket(storeId: Long) =
        connectChatSocketUseCase(storeId).getOrThrow()

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