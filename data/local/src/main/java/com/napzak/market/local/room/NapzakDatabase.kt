package com.napzak.market.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.entity.ChatMessageEntity

@Database(
    entities = [ChatMessageEntity::class],
    version = 1
)
abstract class NapzakDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}