package com.napzak.market.registration.mapper

import com.napzak.market.common.type.ProductConditionType.Companion.fromCondition
import com.napzak.market.registration.dto.ProductPhotoDto
import com.napzak.market.registration.dto.PurchaseRegistrationRequest
import com.napzak.market.registration.dto.PurchaseRegistrationResponse
import com.napzak.market.registration.dto.SaleRegistrationRequest
import com.napzak.market.registration.dto.SaleRegistrationResponse
import com.napzak.market.registration.model.ProductImage
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct

fun ProductImage.toData() = ProductPhotoDto(
    photoId = photoId,
    photoUrl = imageUrl,
    sequence = sequence,
)

fun PurchaseRegistrationProduct.toData() = PurchaseRegistrationRequest(
    productPhotoDto = imageUrls.map(ProductImage::toData),
    genreId = genreId,
    title = title.trim(),
    description = description,
    price = price,
    isPriceNegotiable = isPriceNegotiable,
)

fun SaleRegistrationProduct.toData() = SaleRegistrationRequest(
    productPhotoDto = imageUrls.map(ProductImage::toData),
    genreId = genreId,
    title = title.trim(),
    description = description,
    price = price,
    productCondition = fromCondition(productCondition),
    isDeliveryIncluded = isDeliveryIncluded,
    standardDeliveryFee = standardDeliveryFee,
    halfDeliveryFee = halfDeliveryFee,
)

fun ProductPhotoDto.toDomain() = ProductImage(
    photoId = photoId,
    imageUrl = photoUrl,
    sequence = sequence,
)

fun PurchaseRegistrationResponse.toDomain() = PurchaseRegistrationProduct(
    imageUrls = productPhotoDto.map(ProductPhotoDto::toDomain),
    genreId = genreId,
    genreName = genreName,
    title = title,
    description = description,
    price = price,
    isPriceNegotiable = isPriceNegotiable,
)

fun SaleRegistrationResponse.toDomain() = SaleRegistrationProduct(
    imageUrls = productPhotoDto.map(ProductPhotoDto::toDomain),
    genreId = genreId,
    genreName = genreName,
    title = title,
    description = description,
    price = price,
    productCondition = productCondition,
    isDeliveryIncluded = isDeliveryIncluded,
    standardDeliveryFee = standardDeliveryFee,
    halfDeliveryFee = halfDeliveryFee,
)
