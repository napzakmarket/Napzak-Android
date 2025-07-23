package com.napzak.market.chat.datasource

import com.napzak.market.chat.dto.ChatRoomCreateRequest
import com.napzak.market.chat.dto.ChatRoomCreateResponse
import com.napzak.market.chat.dto.ChatRoomEnterResponse
import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.dto.ChatRoomMessagesResponse
import com.napzak.market.chat.dto.ChatRoomPatchProductRequest
import com.napzak.market.chat.dto.ChatRoomPatchProductResponse
import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.remote.model.EmptyDataResponse
import javax.inject.Inject

class ChatRoomDataSource @Inject constructor(
    private val chatRoomService: ChatRoomService,
) {
    suspend fun getChatRoomInformation(
        roomId: Long,
    ): BaseResponse<ChatRoomInformationResponse> {
        return chatRoomService.getProductChatInformation(roomId)
    }

    suspend fun createChatRoom(
        request: ChatRoomCreateRequest,
    ): BaseResponse<ChatRoomCreateResponse> {
        return chatRoomService.createChatRoom(request)
    }

    suspend fun getChatRoomMessages(
        roomId: Long,
        cursor: String? = null,
        size: Int? = null,
    ): BaseResponse<ChatRoomMessagesResponse> {
        return chatRoomService.getChatRoomMessages(roomId, cursor, size)
    }

    suspend fun enterChatRoom(
        roomId: Long,
    ): BaseResponse<ChatRoomEnterResponse> {
        return chatRoomService.enterChatRoom(roomId)
    }

    suspend fun leaveChatRoom(
        roomId: Long,
    ): EmptyDataResponse {
        return chatRoomService.leaveChatRoom(roomId)
    }

    suspend fun withdrawChatRoom(
        roomId: Long,
    ): EmptyDataResponse {
        return chatRoomService.withdrawChatRoom(roomId)
    }

    suspend fun patchChatRoomProduct(
        roomId: Long,
        request: ChatRoomPatchProductRequest,
    ): BaseResponse<ChatRoomPatchProductResponse> {
        return chatRoomService.patchChatRoomProduct(roomId, request)
    }
}
