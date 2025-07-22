package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.dto.ChatRoomCreateRequest
import com.napzak.market.chat.dto.ChatRoomPatchProductRequest
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.model.ChatItem
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.util.android.suspendRunCatching
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDataSource: ChatRoomDataSource,
) : ChatRoomRepository {
    override suspend fun getChatRoomInformation(
        roomId: Long,
    ): Result<ChatRoomInformation> {
        return suspendRunCatching {
            val response = chatRoomDataSource.getChatRoomInformation(roomId)
            with(response.data) {
                val productBrief = productInfo.toDomain()
                val storeBrief = storeInfo.toDomain()
                ChatRoomInformation(roomId, productBrief, storeBrief)
            }
        }
    }

    override suspend fun createChatRoom(
        productId: Long,
        receiverId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val request = ChatRoomCreateRequest(productId, receiverId)
            val response = chatRoomDataSource.createChatRoom(request)
            response.data.roomId
        }
    }

    override suspend fun getChatRoomMessages(
        roomId: Long,
    ): Result<List<ChatItem<*>>> {
        return suspendRunCatching {
            val response = chatRoomDataSource.getChatRoomMessages(roomId)
            response.data.messages.map { it.toDomain() }
        }
    }

    override suspend fun enterChatRoom(
        roomId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val response = chatRoomDataSource.enterChatRoom(roomId)
            response.data.productId
        }
    }

    override suspend fun leaveChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            chatRoomDataSource.leaveChatRoom(roomId)
        }
    }

    override suspend fun withdrawChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            chatRoomDataSource.withdrawChatRoom(roomId)
        }
    }

    override suspend fun patchChatRoomProduct(
        roomId: Long,
        productId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val request = ChatRoomPatchProductRequest(productId)
            val response = chatRoomDataSource.patchChatRoomProduct(roomId, request)
            response.data.updatedProductId
        }
    }
}
