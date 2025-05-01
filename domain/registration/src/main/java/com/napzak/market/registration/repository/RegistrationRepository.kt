package com.napzak.market.registration.repository

import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct

interface RegistrationRepository {
    suspend fun postSaleRegistration(
        saleRegistrationProduct: SaleRegistrationProduct,
    ): Result<Long>

    suspend fun getSaleRegistration(
        productId: Long,
    ): Result<SaleRegistrationProduct>

    suspend fun putSaleRegistration(
        productId: Long,
        saleRegistrationProduct: SaleRegistrationProduct,
    ): Result<Unit>

    suspend fun postPurchaseRegistration(
        purchaseRegistrationProduct: PurchaseRegistrationProduct,
    ): Result<Long>

    suspend fun getPurchaseRegistration(
        productId: Long,
    ): Result<PurchaseRegistrationProduct>

    suspend fun putPurchaseRegistration(
        productId: Long,
        purchaseRegistrationProduct: PurchaseRegistrationProduct,
    ): Result<Unit>
}
