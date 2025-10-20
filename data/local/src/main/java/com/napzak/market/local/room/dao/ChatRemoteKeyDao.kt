package com.napzak.market.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.napzak.market.local.room.entity.ChatRemoteKeyEntity

@Dao
interface ChatRemoteKeyDao {
    @Upsert
    suspend fun upsertKey(remoteKey: ChatRemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE roomId = :roomId")
    suspend fun getRemoteKey(roomId: Long): ChatRemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE roomId = :roomId")
    suspend fun deleteRemoteKey(roomId: Long)
}