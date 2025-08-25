package com.napzak.market.mypage.mypage.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.ic_circle_favorite
import com.napzak.market.designsystem.R.drawable.ic_circle_help
import com.napzak.market.designsystem.R.drawable.ic_circle_purple_setting
import com.napzak.market.feature.mypage.R.string.mypage_favorites
import com.napzak.market.feature.mypage.R.string.mypage_help_center
import com.napzak.market.feature.mypage.R.string.mypage_settings

internal enum class MyPageMenu(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
) {
    /*SALES(mypage_sales_history, ic_circle_sales),
    PURCHASE(mypage_purchase_history, ic_circle_cart),
    RECENT(mypage_recently_viewed, ic_circle_recent),*/
    FAVORITE(mypage_favorites, ic_circle_favorite),
    SETTINGS(mypage_settings, ic_circle_purple_setting),
    HELP(mypage_help_center, ic_circle_help)
}