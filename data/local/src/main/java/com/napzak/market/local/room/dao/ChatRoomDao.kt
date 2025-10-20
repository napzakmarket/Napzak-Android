package com.napzak.market.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.napzak.market.local.room.entity.ChatRoomEntity

@Dao
interface ChatRoomDao {
    @Query("SELECT * FROM chat_room")
    fun getAllChatRooms(): List<ChatRoomEntity>

    @Query("SELECT * FROM chat_room WHERE roomId = :roomId")
    fun getChatRoom(roomId: Long): ChatRoomEntity?

    @Upsert
    fun upsertChatRoom(chatRoom: ChatRoomEntity)

    @Query("DELETE FROM chat_room WHERE roomId = :roomId")
    fun deleteChatRoom(roomId: Long)
}