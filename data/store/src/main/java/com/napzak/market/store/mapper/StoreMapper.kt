package com.napzak.market.store.mapper

import com.napzak.market.store.dto.StoreResponse
import com.napzak.market.store.model.StoreInfo

fun StoreResponse.toDomain(): StoreInfo = StoreInfo(
    storeId = storeId,
    nickname = storeNickname,
    photoUrl = storePhoto,
    salesCount = totalSellCount,
    purchaseCount = totalBuyCount,
    serviceLink = serviceLink
)