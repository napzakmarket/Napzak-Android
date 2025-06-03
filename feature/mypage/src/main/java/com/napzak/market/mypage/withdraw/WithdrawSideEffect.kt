package com.napzak.market.mypage.withdraw

internal sealed class WithdrawSideEffect {
    data object WithdrawComplete : WithdrawSideEffect()
}