package com.napzak.market.mypage.signout

sealed class SignOutSideEffect {
    data object SignOutComplete : SignOutSideEffect()
}