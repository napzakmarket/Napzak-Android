package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatRoomListResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatService {
    @GET("chat/rooms")
    suspend fun getChatRooms(
        @Query("deviceToken") deviceToken: String?,
    ): BaseResponse<ChatRoomListResponse>
}
