package com.napzak.market.mypage.state

data class MyPageUiState(
    val nickname: String = "",
    val profileImageUrl: String = "",
    val salesCount: Int = 0,
    val purchaseCount: Int = 0
)