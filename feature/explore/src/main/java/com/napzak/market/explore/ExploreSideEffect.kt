package com.napzak.market.explore

sealed interface ExploreSideEffect {
    data object ShowHeartToast : ExploreSideEffect
    data object CancelToast : ExploreSideEffect
}
