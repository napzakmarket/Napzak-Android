package com.napzak.market.wishlist

sealed interface WishlistSideEffect {
    data object ShowHeartToast : WishlistSideEffect
    data object CancelToast : WishlistSideEffect
}
