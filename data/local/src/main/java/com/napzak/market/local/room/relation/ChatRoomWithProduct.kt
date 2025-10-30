package com.napzak.market.local.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.entity.ChatRoomEntity

data class ChatRoomWithProduct(
    @Embedded
    val room: ChatRoomEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ChatProductEntity?
)