package com.napzak.market.product.repository

import com.napzak.market.product.model.Product

interface ProductInterestRepository {
    suspend fun getInterestSellProducts(cursor: String?): Result<Pair<List<Product>, String?>>
    suspend fun getInterestBuyProducts(cursor: String?): Result<Pair<List<Product>, String?>>
}
