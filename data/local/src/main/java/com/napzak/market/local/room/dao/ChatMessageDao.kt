package com.napzak.market.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.relation.ChatMessageWithProduct
import com.napzak.market.local.room.type.ChatStatusType
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(entities: List<ChatMessageEntity>)

    @Query(
        """
        SELECT m.*, p.* FROM chat_messages as m
        LEFT JOIN chat_product as p ON m.productId = p.productId
        WHERE m.roomId = :roomId 
        ORDER BY m.messageId DESC
    """
    )
    fun getChatMessagesWithProducts(roomId: Long): PagingSource<Int, ChatMessageWithProduct>

    @Query("SELECT * FROM chat_messages WHERE roomId = :roomId ORDER BY messageId DESC LIMIT 1")
    fun getLatestMessage(roomId: Long): ChatMessageEntity?

    @Query("SELECT * FROM chat_messages WHERE roomId = :roomId ORDER BY messageId DESC LIMIT 1")
    fun getLatestMessageAsFlow(roomId: Long): Flow<ChatMessageEntity?>

    @Query("DELETE FROM chat_messages WHERE roomId = :roomId")
    suspend fun deleteChatMessages(roomId: Long)

    @Transaction
    suspend fun updateMessageIdAndStatusByUuid(entity: ChatMessageEntity) {
        if (entity.uuid != null && entity.messageId != null) {
            updateMessageIdAndStatusInternal(entity.uuid, entity.messageId)
        }
    }

    @Query("UPDATE chat_messages SET messageId = :messageId, status = :status WHERE uuid = :uuid")
    suspend fun updateMessageIdAndStatusInternal(
        uuid: String,
        messageId: Long,
        status: ChatStatusType = ChatStatusType.RECEIVED
    )

    @Query(
        """
        UPDATE chat_messages SET isRead = 1 
        WHERE roomId = :roomId AND isMessageOwner = :isMessageOwner AND isRead = 0
        """
    )
    suspend fun markMessagesAsRead(roomId: Long, isMessageOwner: Boolean)
}