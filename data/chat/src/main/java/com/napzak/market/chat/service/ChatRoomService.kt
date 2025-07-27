package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatRoomCreateRequest
import com.napzak.market.chat.dto.ChatRoomCreateResponse
import com.napzak.market.chat.dto.ChatRoomEnterResponse
import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.dto.ChatRoomMessagesResponse
import com.napzak.market.chat.dto.ChatRoomPatchProductRequest
import com.napzak.market.chat.dto.ChatRoomPatchProductResponse
import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.remote.model.EmptyDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatRoomService {
    @GET("products/chat/{productId}")
    suspend fun getProductChatInformation(
        @Path("productId") productId: Long
    ): BaseResponse<ChatRoomInformationResponse>

    @POST("chat/rooms")
    suspend fun createChatRoom(
        @Body request: ChatRoomCreateRequest
    ): BaseResponse<ChatRoomCreateResponse>

    @GET("chat/rooms/{roomId}/messages")
    suspend fun getChatRoomMessages(
        @Path("roomId") roomId: Long,
        @Query("cursor") cursor: String?,
        @Query("size") size: Int?
    ): BaseResponse<ChatRoomMessagesResponse>

    @PATCH("chat/rooms/{roomId}/enter")
    suspend fun enterChatRoom(
        @Path("roomId") roomId: Long,
    ): BaseResponse<ChatRoomEnterResponse>

    @PATCH("chat/rooms/{roomId}/leave")
    suspend fun leaveChatRoom(
        @Path("roomId") roomId: Long,
    ): EmptyDataResponse

    @PATCH("chat/rooms/{roomId}/exit")
    suspend fun withdrawChatRoom(
        @Path("roomId") roomId: Long,
    ): EmptyDataResponse

    @PATCH("chat/rooms/{roomId}/product-id")
    suspend fun patchChatRoomProduct(
        @Path("roomId") roomId: Long,
        @Body request: ChatRoomPatchProductRequest
    ): BaseResponse<ChatRoomPatchProductResponse>
}