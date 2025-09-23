package com.napzak.market.store.store

sealed interface StoreSideEffect {
    data object ShowHeartToast : StoreSideEffect
    data class ShowBlockToast(val isStoreBlocked: Boolean) : StoreSideEffect
    data object CancelToast : StoreSideEffect
}
