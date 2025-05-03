package com.napzak.market.product.repository

import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product
import com.napzak.market.product.model.SearchParameters

interface ProductExploreRepository {
    suspend fun getExploreSellProducts(parameters: ExploreParameters): Result<Pair<Int, List<Product>>>
    suspend fun getExploreBuyProducts(parameters: ExploreParameters): Result<Pair<Int, List<Product>>>
    suspend fun getSearchSellProducts(parameters: SearchParameters): Result<Pair<Int, List<Product>>>
    suspend fun getSearchBuyProducts(parameters: SearchParameters): Result<Pair<Int, List<Product>>>
}