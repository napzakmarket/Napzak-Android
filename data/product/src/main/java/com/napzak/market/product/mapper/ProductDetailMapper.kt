package com.napzak.market.product.mapper

import com.napzak.market.product.dto.ProductGetDetailResponse
import com.napzak.market.product.dto.ProductGetDetailResponse.ProductPhotoResponse
import com.napzak.market.product.dto.ProductGetDetailResponse.StoreInfoResponse
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.model.ProductDetail.ProductPhoto
import com.napzak.market.product.model.ProductDetail.StoreInfo

fun ProductGetDetailResponse.toDomain(isInterested: Boolean): ProductDetail =
    with(this.productDetail) {
        ProductDetail(
            productId = productId,
            tradeType = tradeType,
            genreName = genreName,
            productName = productName,
            price = price,
            uploadTime = uploadTime,
            chatCount = chatCount,
            interestCount = interestCount,
            description = description,
            productCondition = productCondition,
            standardDeliveryFee = standardDeliveryFee,
            halfDeliveryFee = halfDeliveryFee,
            isDeliveryIncluded = isDeliveryIncluded,
            isPriceNegotiable = isPriceNegotiable,
            tradeStatus = tradeStatus,
            isOwnedByCurrentUser = isOwnedByCurrentUser,
            isInterested = isInterested,
            productPhotos = productPhotoList.toDomain(),
            storeInfo = storeInfo.toDomain(),
        )
    }

fun List<ProductPhotoResponse>.toDomain(): List<ProductPhoto> = map { productPhotoResponse ->
    with(productPhotoResponse) {
        ProductPhoto(
            photoId = photoId,
            photoUrl = photoUrl,
            photoSequence = photoSequence,
        )
    }
}

fun StoreInfoResponse.toDomain(): StoreInfo =
    StoreInfo(
        userId = userId,
        storePhoto = storePhoto,
        nickname = nickname,
        totalSellCount = totalSellCount,
        totalBuyCount = totalBuyCount,
    )