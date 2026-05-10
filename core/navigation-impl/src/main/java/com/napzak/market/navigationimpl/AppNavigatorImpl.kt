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
import com.napzak.market.navigation.keys.SplashScreenKey
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    override val backStack: NavBackStack<ScreenKey>,
    private val chatSessionManager: ChatSessionManager,
    private val deepLinkEventBus: DeepLinkEventBus,
) : AppNavigator {
    override val currentScreen get() = backStack.lastOrNull()

    /**
     * [ScreenKey]와 매핑된 화면으로 이동합니다.
     *
     * @param key 이동할 화면의 [ScreenKey]
     * @param popUpTo 이동 시 스택에서 제거할 화면의 [ScreenKey]
     * @param inclusive [popUpTo]가 스택에서 제거되는 방식을 결정합니다. true면 popUpTo까지 제거합니다.
     * @param singleTop 화면의 중복 가능 여부를 결정합니다.
     */
    override fun navigateTo(
        key: ScreenKey,
        popUpTo: ScreenKey?,
        inclusive: Boolean,
        singleTop: Boolean,
    ) {
        if (singleTop && currentScreen == key) return

        var popUpToIndex = backStack.lastIndexOf(popUpTo)

        if (popUpToIndex >= 0) {
            if (!inclusive) popUpToIndex++

            while (backStack.size > popUpToIndex) {
                backStack.removeLastOrNull()
            }
        }

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
                    if (currentScreen != LoginScreenKey && currentScreen != SplashScreenKey) {
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
