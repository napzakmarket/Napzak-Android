package com.napzak.market.navigationimpl

import android.content.Intent
import androidx.navigation3.runtime.NavBackStack
import com.napzak.market.event.ChatSessionManager
import com.napzak.market.event.DeepLinkEvent
import com.napzak.market.event.DeepLinkEventBus
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.keys.ChatListScreenKey
import com.napzak.market.navigation.keys.ChatRoomScreenKey
import com.napzak.market.navigation.keys.LoginScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    override val backStack: NavBackStack<ScreenKey>,
    private val chatSessionManager: ChatSessionManager,
    private val deepLinkEventBus: DeepLinkEventBus,
) : AppNavigator {
    private val currentScreen = backStack.lastOrNull()

    override fun navigateTo(
        key: ScreenKey,
    ) {
        if (backStack.lastOrNull() == key) return
        backStack.add(key)
    }

    override fun pop() {
        backStack.removeLastOrNull()
    }

    // Notification의 PendingIntent 처리를 관리합니다.
    override suspend fun handleIntent(intent: Intent) {
        val notifyType = intent.getStringExtra("type")
        when (notifyType) {
            "chat" -> handleChatIntent(intent)
        }
    }

    private suspend fun handleChatIntent(intent: Intent) {
        val chatRoomId = intent.getStringExtra("roomId")?.toLongOrNull()

        if (chatRoomId != null) {
            chatSessionManager.setChatRoomId(chatRoomId)
            deepLinkEventBus.send(DeepLinkEvent.NavigateToChatRoom(chatRoomId))
        }
    }

    override suspend fun handleDeepLinkEvent() {
        deepLinkEventBus.events.collect { event ->
            when (event) {
                is DeepLinkEvent.NavigateToChatRoom -> {
                    if (currentScreen != LoginScreenKey) {
                        backStack.addAll(
                            listOf(
                                ChatListScreenKey,
                                ChatRoomScreenKey(event.chatRoomId)
                            )
                        )
                        chatSessionManager.setChatRoomId(null)
                    }
                }
            }
        }
    }
}
