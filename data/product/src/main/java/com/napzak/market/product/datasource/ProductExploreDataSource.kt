package com.napzak.market.product.datasource

import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.SearchParameters
import com.napzak.market.product.service.ProductExploreService
import javax.inject.Inject

class ProductExploreDataSource @Inject constructor(
    private val productExploreService: ProductExploreService,
) {
    suspend fun getExploreSellProducts(parameters: ExploreParameters) =
        productExploreService.getExploreSellProducts(
            sort = parameters.sort,
            genreId = parameters.genreId,
            isOnSale = parameters.isOnSale,
            isUnopened = parameters.isUnopened,
            cursor = parameters.cursor,
        )

    suspend fun getExploreBuyProducts(parameters: ExploreParameters) =
        productExploreService.getExploreBuyProducts(
            sort = parameters.sort,
            genreId = parameters.genreId,
            isOnSale = parameters.isOnSale,
            cursor = parameters.cursor,
        )

    suspend fun getSearchSellProducts(parameters: SearchParameters) =
        productExploreService.getSearchSellProducts(
            searchWord = parameters.searchWord,
            sort = parameters.sort,
            genreId = parameters.genreId,
            isOnSale = parameters.isOnSale,
            isUnopened = parameters.isUnopened,
            cursor = parameters.cursor,
        )

    suspend fun getSearchBuyProducts(parameters: SearchParameters) =
        productExploreService.getSearchBuyProducts(
            searchWord = parameters.searchWord,
            sort = parameters.sort,
            genreId = parameters.genreId,
            isOnSale = parameters.isOnSale,
            cursor = parameters.cursor,
        )
}
