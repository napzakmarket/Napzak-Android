package com.napzak.market.registration.usecase

import com.napzak.market.registration.model.Product
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

class RegisterProductUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(product: Product): Result<Long> {
        return when (product) {
            is SaleRegistrationProduct -> registrationRepository.postSaleRegistration(product)
            is PurchaseRegistrationProduct -> registrationRepository.postPurchaseRegistration(product)
            else -> Result.failure(IllegalArgumentException("Invalid product type"))
        }
    }
}
