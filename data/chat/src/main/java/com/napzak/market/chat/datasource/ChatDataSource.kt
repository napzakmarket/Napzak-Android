package com.napzak.market.chat.datasource

import com.napzak.market.chat.dto.ChatRoomListResponse
import com.napzak.market.chat.service.ChatService
import com.napzak.market.remote.model.BaseResponse
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatService: ChatService,
) {
    suspend fun getChatRooms(
        deviceToken: String?,
    ): BaseResponse<ChatRoomListResponse> {
        return chatService.getChatRooms(deviceToken)
    }
}
