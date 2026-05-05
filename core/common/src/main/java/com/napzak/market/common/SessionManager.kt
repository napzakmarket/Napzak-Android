package com.napzak.market.common

object SessionManager {
    var chatRoomId: Long? = null
    var isPhoneChecked: Boolean = false

    fun clearChatRoomId() {
        chatRoomId = null
    }
}