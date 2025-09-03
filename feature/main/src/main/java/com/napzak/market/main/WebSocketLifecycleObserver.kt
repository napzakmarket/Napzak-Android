package com.napzak.market.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.napzak.market.chat.usecase.ConnectChatSocketUseCase
import com.napzak.market.chat.usecase.DisconnectChatSocketUseCase
import com.napzak.market.chat.usecase.SubscribeChatRoomsUseCase
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.TokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebSocketLifecycleObserver @Inject constructor(
    private val storeRepository: StoreRepository,
    private val tokenProvider: TokenProvider,
    private val connectChatSocketUseCase: ConnectChatSocketUseCase,
    private val disconnectChatSocketUseCase: DisconnectChatSocketUseCase,
    private val subscribeChatRoomsUseCase: SubscribeChatRoomsUseCase,
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

    private suspend fun fetchStoreInfo(): StoreInfo? {
        return storeRepository.fetchStoreInfo().getOrNull()
    }

    private suspend fun connectChatSocket(storeId: Long) {
        connectChatSocketUseCase(storeId)
    }

    private suspend fun subscribeChatRooms(storeId: Long) {
        subscribeChatRoomsUseCase(storeId = storeId)
    }

    private suspend fun isTokenAvailable() = tokenProvider.getAccessToken() != null
}