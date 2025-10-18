package com.napzak.market.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.napzak.market.local.room.entity.ChatProductEntity

@Dao
interface ChatProductDao {
    @Query("SELECT * FROM chat_product WHERE productId = :productId")
    fun getProduct(productId: Long): ChatProductEntity?

    @Upsert
    fun upsertProduct(product: ChatProductEntity)

    @Query("DELETE FROM chat_product WHERE productId = :productId")
    fun deleteProduct(productId: Long)
}
