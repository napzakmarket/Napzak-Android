package com.napzak.market.mypage.setting.navigation

import androidx.navigation.NavController

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings() {
    this.navigate(SETTINGS_ROUTE)
}