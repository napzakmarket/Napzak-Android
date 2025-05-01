package com.napzak.market.product.mapper

import com.napzak.market.product.dto.ProductHomeBuyResponse
import com.napzak.market.product.dto.ProductHomeRecommendResponse
import com.napzak.market.product.dto.ProductHomeSellResponse
import com.napzak.market.product.dto.ProductResponse
import com.napzak.market.product.model.Product

fun ProductHomeRecommendResponse.toProducts() = products.map { product -> product.toDomain() }
fun ProductHomeSellResponse.toProducts() = products.map { product -> product.toDomain() }
fun ProductHomeBuyResponse.toProducts() = products.map { product -> product.toDomain() }

fun List<ProductResponse>.toProducts() = map { product -> product.toDomain() }

fun ProductResponse.toDomain() = with(this) {
    Product(
        productId = productId ?: 0,
        genreName = genreName ?: "",
        productName = productName ?: "",
        photo = photo ?: "",
        price = price ?: 0,
        uploadTime = uploadTime ?: "",
        isInterested = isInterested ?: false,
        tradeType = tradeType ?: "",
        tradeStatus = tradeStatus ?: "",
        isPriceNegotiable = isPriceNegotiable ?: false,
        isOwnedByCurrentUser = isOwnedByCurrentUser ?: false,
        interestCount = interestCount ?: 0,
        chatCount = chatCount ?: 0,
    )
}