package com.napzak.market.registration.model

interface Product

data class SaleRegistrationProduct(
    val imageUrls: List<ProductImage>,
    val genreId: Long,
    val genreName: String,
    val title: String,
    val description: String,
    val price: Int,
    val productCondition: String?,
    val isDeliveryIncluded: Boolean,
    val standardDeliveryFee: Int,
    val halfDeliveryFee: Int,
) : Product

data class PurchaseRegistrationProduct(
    val imageUrls: List<ProductImage>,
    val genreId: Long,
    val genreName: String,
    val title: String,
    val description: String,
    val price: Int,
    val isPriceNegotiable: Boolean,
) : Product

data class ProductImage(
    val photoId: Long? = null,
    val imageUrl: String,
    val sequence: Int,
)
