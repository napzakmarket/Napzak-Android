package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data class StoreScreenKey(
    val storeId: Long,
) : ScreenKey

@Serializable
data class EditStoreScreenKey(
    val storeId: Long,
) : ScreenKey
