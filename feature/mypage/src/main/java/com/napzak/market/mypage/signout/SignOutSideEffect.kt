package com.napzak.market.mypage.signout

internal sealed class SignOutSideEffect {
    data object SignOutComplete : SignOutSideEffect()
}