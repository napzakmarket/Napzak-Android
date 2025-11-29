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
    suspend fun upsertProduct(product: ChatProductEntity)

    suspend fun updateProductPartially(product: ChatProductEntity) {
        updateProduct(
            productId = product.productId,
            tradeType = product.tradeType,
            title = product.title,
            price = product.price,
            genreName = product.genreName,
            isProductDeleted = product.isProductDeleted
        )
    }

    @Query(
        """
        UPDATE chat_product SET
            tradeType = :tradeType,
            title = :title,
            price = :price,
            genreName = :genreName,
            isProductDeleted = :isProductDeleted
        WHERE productId = :productId
    """
    )
    suspend fun updateProduct(
        productId: Long,
        tradeType: String,
        title: String,
        price: Int,
        genreName: String,
        isProductDeleted: Boolean,
    )

    @Query("DELETE FROM chat_product WHERE productId = :productId")
    fun deleteProduct(productId: Long)
}
