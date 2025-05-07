package com.napzak.market.registration.sale.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.type.ProductConditionType

class SaleContract {
    @Immutable
    data class SaleUiState(
        val condition: ProductConditionType? = null,
        val isShippingFeeIncluded: Boolean? = null,
        val isNormalShippingChecked: Boolean = false,
        val normalShippingFee: String = "",
        val isHalfShippingChecked: Boolean = false,
        val halfShippingFee: String = "",
    )
}
