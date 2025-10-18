package com.napzak.market.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.entity.ChatRemoteKeyEntity

@Database(
    entities = [
        ChatMessageEntity::class,
        ChatRemoteKeyEntity::class,
        ChatProductEntity::class,
    ],
    version = 1
)
abstract class NapzakDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatRemoteKeyDao(): ChatRemoteKeyDao
    abstract fun chatProductDao(): ChatProductDao
}