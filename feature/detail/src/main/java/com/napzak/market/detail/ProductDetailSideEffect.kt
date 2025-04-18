package com.napzak.market.detail

sealed interface ProductDetailSideEffect {
    data object NavigateUp : ProductDetailSideEffect
}