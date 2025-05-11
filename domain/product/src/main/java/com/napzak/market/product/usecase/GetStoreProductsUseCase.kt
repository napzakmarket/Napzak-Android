package com.napzak.market.product.usecase

import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductStoreRepository
import javax.inject.Inject

class GetStoreProductsUseCase @Inject constructor(
    private val productStoreRepository: ProductStoreRepository,
) {
    suspend operator fun invoke(
        storeId: Long,
        isMarketTypeSell: Boolean,
        filteredGenres: List<Long>,
        isOnSale: Boolean,
        sortOption: String,
    ): Result<Pair<Int, List<Product>>> {
        val parameters = ExploreParameters(
            sort = sortOption,
            genreIds = filteredGenres,
            isOnSale = isOnSale,
            cursor = null,
        )

        return if (isMarketTypeSell) {
            productStoreRepository.getStoreSellProducts(storeId, parameters)
        } else {
            productStoreRepository.getStoreBuyProducts(storeId, parameters)
        }
    }
}
