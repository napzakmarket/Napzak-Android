package com.napzak.market.registration.model

interface Product

data class SaleProduct(
    val imageUrls: List<ProductImage>,
    val genreId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val productCondition: String?,
    val isDeliveryIncluded: Boolean,
    val standardDeliveryFee: Int,
    val halfDeliveryFee: Int,
) : Product

data class PurchaseProduct(
    val imageUrls: List<ProductImage>,
    val genreId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val isPriceNegotiable: Boolean,
) : Product

data class ProductImage(
    val photoId: Long?,
    val imageUrl: String,
    val sequence: Int,
)
