package com.napzak.market.store.store

sealed interface StoreSideEffect {
    data object ShowHeartToast : StoreSideEffect
    data object CancelToast : StoreSideEffect
}
