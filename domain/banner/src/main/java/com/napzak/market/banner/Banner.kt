package com.napzak.market.banner

data class Banner(
    val id: Long,
    val imageUrl: String,
    val linkUrl: String,
    val sequence: Int,
    val isExternal: Boolean,
)
