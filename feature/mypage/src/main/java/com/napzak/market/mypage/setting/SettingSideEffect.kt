package com.napzak.market.mypage.setting

sealed class SettingSideEffect {
    data object OnSignOutComplete : SettingSideEffect()
}