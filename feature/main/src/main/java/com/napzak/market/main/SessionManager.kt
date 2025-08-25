package com.napzak.market.main

object SessionManager {
    var chatRoomId: Long? = null

    fun clearChatRoomId() {
        chatRoomId = null
    }
}
