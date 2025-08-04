package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief

internal fun ChatRoomInformationResponse.toDomain(): ChatRoomInformation = ChatRoomInformation(
    roomId = roomId,
    productBrief = productInfo.toDomain(),
    storeBrief = storeInfo.toDomain(),
)

private fun ChatRoomInformationResponse.ProductInfo.toDomain(): ProductBrief = ProductBrief(
    productId = productId,
    photo = photo,
    tradeType = tradeType,
    title = title,
    price = price,
    isPriceNegotiable = isPriceNegotiable,
    genreName = genreName,
    productOwnerId = productOwnerId,
    isMyProduct = isMyProduct,
)

private fun ChatRoomInformationResponse.StoreInfo.toDomain(): StoreBrief = StoreBrief(
    storeId = storeId,
    nickname = nickname,
    storePhoto = storePhoto,
    isWithdrawn = isWithdrawn,
)
