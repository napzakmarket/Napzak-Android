package com.napzak.market.local.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatMessageWithProduct(
    @Embedded
    val message: ChatMessageEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ChatProductEntity?
)
