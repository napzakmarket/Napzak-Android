package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatRoomService {
    @GET("products/chat/{roomId}")
    suspend fun getProductChatInformation(
        @Path("roomId") roomId: Long
    ): BaseResponse<ChatRoomInformationResponse>
}