package com.napzak.market.mypage.setting

internal sealed class SettingSideEffect {
    data object OnSignOutComplete : SettingSideEffect()
}