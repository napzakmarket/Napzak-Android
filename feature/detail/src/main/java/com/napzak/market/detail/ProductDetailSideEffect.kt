package com.napzak.market.detail

import com.napzak.market.detail.type.ProductDetailToastType

sealed interface ProductDetailSideEffect {
    data class ShowToast(
        val productDetailToastType: ProductDetailToastType,
        val message: String = "",
    ) : ProductDetailSideEffect

    data object CancelToast : ProductDetailSideEffect
    data object NavigateUp : ProductDetailSideEffect
}