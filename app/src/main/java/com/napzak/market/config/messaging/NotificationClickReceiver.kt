package com.napzak.market.config.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.napzak.market.main.ChatDeepLinkEvent
import com.napzak.market.main.ChatDeepLinkEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

