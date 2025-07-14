package com.napzak.market.chat.datasource

import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.remote.model.BaseResponse
import javax.inject.Inject

class ChatRoomDataSource @Inject constructor(
    private val chatRoomService: ChatRoomService,
) {
    suspend fun getChatRoomInformation(roomId: Long): BaseResponse<ChatRoomInformationResponse> =
        chatRoomService.getProductChatInformation(roomId)
}
