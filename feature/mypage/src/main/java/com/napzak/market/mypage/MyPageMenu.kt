package com.napzak.market.mypage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.*
import com.napzak.market.feature.mypage.R.string.*

enum class MyPageMenu(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
) {
    SALES(sales_history, ic_sales_32),
    PURCHASE(purchase_history, ic_purchase_32),
    RECENT(recently_viewed, ic_recent_30),
    FAVORITE(favorites, ic_favorite_27),
    SETTINGS(settings, ic_settings_27),
    HELP(help_center, ic_help_32)
}