package com.napzak.market.mypage.wishlist

sealed interface WishlistSideEffect {
    data object ShowHeartToast : WishlistSideEffect
    data object CancelToast : WishlistSideEffect
}
