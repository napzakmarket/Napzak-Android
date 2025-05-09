package com.napzak.market.product.model

data class ProductDetail(
    val productId: Long,
    val tradeType: String,
    val genreName: String,
    val productName: String,
    val price: Int,
    val uploadTime: String,
    val chatCount: Int,
    val interestCount: Int,
    val description: String,
    val productCondition: String,
    val standardDeliveryFee: Int,
    val halfDeliveryFee: Int,
    val isDeliveryIncluded: Boolean,
    val isPriceNegotiable: Boolean,
    val tradeStatus: String,
    val isOwnedByCurrentUser: Boolean,
    val isInterested: Boolean,
    val productPhotos: List<ProductPhoto>,
    val storeInfo: StoreInfo,
) {
    data class StoreInfo(
        val userId: Long,
        val storePhoto: String,
        val nickname: String,
        val totalSellCount: Int,
        val totalBuyCount: Int,
    )

    data class ProductPhoto(
        val photoId: Long,
        val photoUrl: String,
        val photoSequence: Int,
    )
}
