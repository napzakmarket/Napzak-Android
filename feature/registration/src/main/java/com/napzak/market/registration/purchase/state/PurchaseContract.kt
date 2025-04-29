package com.napzak.market.registration.purchase.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class PurchaseContract {
    @Immutable
    data class PurchaseUiState (
        val loadState: UiState<Nothing> = UiState.Loading,
        val imageUris: ImmutableList<Uri> = persistentListOf(),
        val genre: String = "",
        val title: String = "",
        val description: String = "",
        val price: String = "",
        val isNegotiable: Boolean = false,
    )

    sealed class PurchaseSideEffect {
        // TODO: API 연결 후 상세 페이지 연동 필요
        data class NavigateToDetail(val productId: Long): PurchaseSideEffect()
    }
}
