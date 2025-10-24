package com.napzak.market.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.napzak.market.local.room.entity.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {
    @Query("SELECT * FROM chat_room WHERE roomId = :roomId")
    fun getChatRoom(roomId: Long): ChatRoomEntity?

    @Query("SELECT * FROM chat_room")
    fun getChatRoomsFlow(): Flow<List<ChatRoomEntity>>

    @Transaction
    suspend fun safeUpsertChatRooms(entities: List<ChatRoomEntity>, isInformation: Boolean) {
        entities.forEach { chatRoom ->
            val existingChatRoom = getChatRoom(chatRoom.roomId)
            if (existingChatRoom == null) {
                upsertChatRoom(chatRoom)
            } else {
                val updatedChatRoom = if (isInformation) {
                    with(chatRoom) {
                        existingChatRoom.copy(
                            opponentNickName = opponentNickName,
                            opponentStorePhoto = opponentStorePhoto,
                            productId = productId,
                            isWithdrawn = isWithdrawn,
                            isReported = isReported,
                            isOpponentStoreBlocked = isOpponentStoreBlocked,
                            isChatBlocked = isChatBlocked,
                        )
                    }
                } else {
                    with(chatRoom) {
                        existingChatRoom.copy(
                            opponentNickName = opponentNickName,
                            opponentStorePhoto = opponentStorePhoto,
                            lastMessage = lastMessage,
                            lastMessageAt = lastMessageAt,
                            unreadCount = unreadCount,
                            isWithdrawn = isWithdrawn,
                        )
                    }
                }

                upsertChatRoom(updatedChatRoom)
            }
        }
    }

    @Upsert
    suspend fun upsertChatRoom(chatRoom: ChatRoomEntity)

    @Query(
        """
        UPDATE chat_room SET
            lastMessage = :lastMessage, 
            lastMessageAt = :lastMessageAt,
            unreadCount = :unreadCount,
            lastUpdated = :lastUpdated
        WHERE roomId = :roomId
        """
    )
    suspend fun updateLastMessage(
        roomId: Long, lastMessage: String, lastMessageAt: Long, unreadCount: Int, lastUpdated: Long,
    )

    @Query("UPDATE chat_room SET productId = :productId WHERE roomId = :roomId")
    suspend fun updateProductId(roomId: Long, productId: Long)

    @Query("UPDATE chat_room SET isOpponentOnline = :isOpponentOnline WHERE roomId = :roomId")
    suspend fun updateOpponentOnlineStatus(roomId: Long, isOpponentOnline: Boolean)

    @Query("UPDATE chat_room SET isWithdrawn = :isWithdrawn WHERE roomId = :roomId")
    suspend fun updateWithdrawnStatus(roomId: Long, isWithdrawn: Boolean)

    @Query("UPDATE chat_room SET isReported = :isReported WHERE roomId = :roomId")
    suspend fun updateStoreReportedStatus(roomId: Long, isReported: Boolean)

    @Query("UPDATE chat_room SET isOpponentStoreBlocked = :isOpponentStoreBlocked WHERE roomId = :roomId")
    suspend fun updateStoreBlockedStatus(roomId: Long, isOpponentStoreBlocked: Boolean)

    @Query("UPDATE chat_room SET isChatBlocked = :isChatBlocked WHERE roomId = :roomId")
    suspend fun updateChatBlockedStatus(roomId: Long, isChatBlocked: Boolean)

    @Query("DELETE FROM chat_room WHERE roomId = :roomId")
    suspend fun deleteChatRoom(roomId: Long)

    @Query("DELETE FROM chat_room")
    suspend fun deleteAllChatRooms()
}