package com.napzak.market.interest.repository

interface InterestProductRepository {
    suspend fun setInterestProduct(productId: Long): Result<Unit>
    suspend fun unsetInterestProduct(productId: Long): Result<Unit>
}