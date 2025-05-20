package com.napzak.market.mypage.mypage.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class MyPageUiState(
    val storeId: Long = 0,
    val nickname: String = "",
    val profileImageUrl: String = "",
    val salesCount: Int = 0,
    val purchaseCount: Int = 0,
    val serviceLink: String = "",
)