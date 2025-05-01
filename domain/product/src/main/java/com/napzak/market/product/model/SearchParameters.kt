package com.napzak.market.product.model

data class SearchParameters(
    val searchWord: String,
    val sort: String,
    val genreId: Long?,
    val isOnSale: Boolean = false,
    val isUnopened: Boolean,
    val cursor: String? = null,
)
