package com.napzak.market.home

interface HomeSideEffect {
    data object ShowInterestToast : HomeSideEffect
    data object CancelInterestToast : HomeSideEffect
}