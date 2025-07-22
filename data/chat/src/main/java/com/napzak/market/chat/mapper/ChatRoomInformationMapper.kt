package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief

internal fun ChatRoomInformationResponse.ProductInfo.toDomain(): ProductBrief = ProductBrief(
    productId = productId,
    photo = photo,
    tradeType = tradeType,
    title = title,
    price = price,
    isPriceNegotiable = isPriceNegotiable,
    genreName = genreName,
)

internal fun ChatRoomInformationResponse.StoreInfo.toDomain(): StoreBrief = StoreBrief(
    storeId = storeId,
    nickname = nickname,
    storePhoto = storePhoto,
    isWithdrawn = isWithdrawn,
)
