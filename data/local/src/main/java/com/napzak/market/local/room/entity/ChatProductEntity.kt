package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_product")
data class ChatProductEntity(
    @PrimaryKey val productId: Long,
    val photo: String? = null,
    val tradeType: String,
    val title: String,
    val price: Int,
    val isPriceNegotiable: Boolean = false,
    val genreName: String,
    val productOwnerId: Long? = null,
    val isMyProduct: Boolean = false,
    val isProductDeleted: Boolean,
)
