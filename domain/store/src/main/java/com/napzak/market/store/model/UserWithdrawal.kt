package com.napzak.market.store.model

data class UserWithdrawal(
    val storeId: Long,
    val title: String,
    val description: String? = null,
)