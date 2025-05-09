package com.napzak.market.registration.purchase.state

import androidx.compose.runtime.Immutable

class PurchaseContract {
    @Immutable
    data class PurchaseUiState(
        val isNegotiable: Boolean = false,
    )
}
