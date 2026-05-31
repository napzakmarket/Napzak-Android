package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailScreenKey(
    val productId: Long,
) : ScreenKey
