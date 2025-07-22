package com.napzak.market.mypage.mypage.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState

@Immutable
internal data class MyPageUiState(
    val loadState: UiState<Unit> = UiState.Empty,
    val storeId: Long = 0,
    val nickname: String = "",
    val profileImageUrl: String = "",
    val salesCount: Int = 0,
    val purchaseCount: Int = 0,
    val serviceLink: String = "",
)