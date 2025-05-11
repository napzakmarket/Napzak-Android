package com.napzak.market.registration.usecase

import com.napzak.market.registration.model.Product
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

class EditRegisteredProductUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(
        productId: Long,
        product: Product,
    ): Result<Unit> {
        return when (product) {
            is SaleRegistrationProduct ->
                registrationRepository.putSaleRegistration(productId, product)

            is PurchaseRegistrationProduct ->
                registrationRepository.putPurchaseRegistration(productId, product)

            else -> Result.failure(IllegalArgumentException("Invalid product type"))
        }
    }
}
