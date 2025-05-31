package com.napzak.market.detail

sealed interface ProductDetailSideEffect {
    data class ShowStatusChangeToast(val tradeStatus: String, val tradeType: String) :
        ProductDetailSideEffect

    data object ShowDeleteToast : ProductDetailSideEffect
    data object ShowHeartToast : ProductDetailSideEffect
    data object NavigateUp : ProductDetailSideEffect
}