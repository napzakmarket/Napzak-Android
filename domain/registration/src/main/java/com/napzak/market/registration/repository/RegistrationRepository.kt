package com.napzak.market.registration.repository

import com.napzak.market.registration.model.PurchaseProduct
import com.napzak.market.registration.model.SaleProduct

interface RegistrationRepository {
    suspend fun postSaleRegistration(
        saleProduct: SaleProduct,
    ): Result<Long>

    suspend fun getSaleRegistration(
        productId: Long,
    ): Result<SaleProduct>

    suspend fun putSaleRegistration(
        productId: Long,
        saleProduct: SaleProduct,
    ): Result<Unit>

    suspend fun postPurchaseRegistration(
        purchaseProduct: PurchaseProduct,
    ): Result<Long>

    suspend fun getPurchaseRegistration(
        productId: Long,
    ): Result<PurchaseProduct>

    suspend fun putPurchaseRegistration(
        productId: Long,
        purchaseProduct: PurchaseProduct,
    ): Result<Unit>
}
