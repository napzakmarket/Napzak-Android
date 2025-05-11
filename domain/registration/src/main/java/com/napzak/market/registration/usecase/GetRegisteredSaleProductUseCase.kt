package com.napzak.market.registration.usecase

import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

class GetRegisteredSaleProductUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(
        productId: Long,
    ): Result<SaleRegistrationProduct> = registrationRepository.getSaleRegistration(productId)
}
