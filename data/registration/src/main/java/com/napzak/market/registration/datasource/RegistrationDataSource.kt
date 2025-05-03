package com.napzak.market.registration.datasource

import com.napzak.market.registration.dto.PurchaseRegistrationRequest
import com.napzak.market.registration.dto.SaleRegistrationRequest
import com.napzak.market.registration.service.RegistrationService
import javax.inject.Inject

class RegistrationDataSource @Inject constructor(
    private val registrationService: RegistrationService,
) {
    suspend fun postSaleRegistration(
        saleRegistrationRequest: SaleRegistrationRequest,
    ) = registrationService.postSaleRegistration(saleRegistrationRequest).data

    suspend fun getSaleRegistration(
        productId: Long,
    ) = registrationService.getSaleRegistration(productId).data

    suspend fun putSaleRegistration(
        productId: Long,
        saleRegistrationRequest: SaleRegistrationRequest,
    ) = registrationService.putSaleRegistration(productId, saleRegistrationRequest).data

    suspend fun postPurchaseRegistration(
        purchaseRegistrationRequest: PurchaseRegistrationRequest,
    ) = registrationService.postPurchaseRegistration(purchaseRegistrationRequest).data

    suspend fun getPurchaseRegistration(
        productId: Long,
    ) = registrationService.getPurchaseRegistration(productId).data

    suspend fun putPurchaseRegistration(
        productId: Long,
        purchaseRegistrationRequest: PurchaseRegistrationRequest,
    ) = registrationService.putPurchaseRegistration(productId, purchaseRegistrationRequest).data
}
