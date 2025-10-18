package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_product")
data class ChatProductEntity(
    @PrimaryKey val productId: Long,
    val tradeType: String,
    val title: String,
    val price: Int,
    val genreName: String,
    val isProductDeleted: Boolean,
)
