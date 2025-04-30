package com.napzak.market.registration.sale.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class SaleContract {
    @Immutable
    data class SaleUiState (
        val loadState: UiState<Nothing> = UiState.Loading,
        val imageUris: ImmutableList<Uri> = persistentListOf(),
        val genre: String = "",
        val title: String = "",
        val description: String = "",
        val condition: ProductConditionType? = null,
        val price: String = "",
        val isShippingFeeIncluded: Boolean? = null,
        val normalShippingFee: String = "",
        val halfShippingFee: String = "",
    )

    sealed class SaleSideEffect {
        // TODO: API 연결 후 상세 페이지 연동 필요
        data class NavigateToDetail(val productId: Long): SaleSideEffect()
    }
}
