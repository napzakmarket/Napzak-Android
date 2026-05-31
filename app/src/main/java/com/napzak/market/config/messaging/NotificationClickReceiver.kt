package com.napzak.market.config.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.napzak.market.main.navigation.ChatDeepLinkEvent
import com.napzak.market.main.navigation.ChatDeepLinkEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Deprecated("현재 알림 처리 로직에는 사용하지 않습니다")
class NotificationClickReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "com.napzak.OPEN_DEEP_LINK") {
            val deepLink = p1.getStringExtra("deep_link")?.toUri()
            if (deepLink != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    when (deepLink.host) {
                        "chat" -> {
                            val chatRoomId = deepLink.lastPathSegment
                            if (chatRoomId != null)
                                ChatDeepLinkEventBus.send(ChatDeepLinkEvent.ChatRoom(chatRoomId))
                        }
                    }
                }
            }
        }
    }
}

