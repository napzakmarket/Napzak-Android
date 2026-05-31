package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data object ChatListScreenKey : MainTabScreenKey

@Serializable
data class ChatRoomScreenKey(
    val chatRoomId: Long? = null,
    val productId: Long? = null,
) : ScreenKey
