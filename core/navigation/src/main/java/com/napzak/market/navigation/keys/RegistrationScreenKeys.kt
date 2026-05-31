package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data object RegistrationScreenKey : MainTabScreenKey

@Serializable
data class SaleRegistrationScreenKey(
    val productId: Long? = null,
) : ScreenKey

@Serializable
data class PurchaseRegistrationScreenKey(
    val productId: Long? = null,
) : ScreenKey

@Serializable
data class GenreSearchScreenKey(
    val from: String,
    val selectedGenreId: Long? = null,
) : ScreenKey
