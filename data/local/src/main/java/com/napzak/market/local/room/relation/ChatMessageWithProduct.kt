package com.napzak.market.local.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity

data class ChatMessageWithProduct(
    @Embedded
    val message: ChatMessageEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ChatProductEntity?
)