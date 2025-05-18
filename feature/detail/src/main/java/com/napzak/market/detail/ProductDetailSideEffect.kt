package com.napzak.market.detail

sealed interface ProductDetailSideEffect {
    data object ShowDeleteSnackBar : ProductDetailSideEffect
    data object NavigateUp : ProductDetailSideEffect
}