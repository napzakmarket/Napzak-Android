package com.napzak.market.registration.usecase

import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

class GetRegisteredPurchaseProductUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(
        productId: Long,
    ): Result<PurchaseRegistrationProduct> = registrationRepository.getPurchaseRegistration(productId)
}
