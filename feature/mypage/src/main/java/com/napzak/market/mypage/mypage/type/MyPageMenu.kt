package com.napzak.market.mypage.mypage.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.ic_favorite_36
import com.napzak.market.designsystem.R.drawable.ic_help_36
import com.napzak.market.designsystem.R.drawable.ic_purchase_36
import com.napzak.market.designsystem.R.drawable.ic_recent_36
import com.napzak.market.designsystem.R.drawable.ic_sales_36
import com.napzak.market.designsystem.R.drawable.ic_settings_36
import com.napzak.market.feature.mypage.R.string.mypage_favorites
import com.napzak.market.feature.mypage.R.string.mypage_help_center
import com.napzak.market.feature.mypage.R.string.mypage_purchase_history
import com.napzak.market.feature.mypage.R.string.mypage_recently_viewed
import com.napzak.market.feature.mypage.R.string.mypage_sales_history
import com.napzak.market.feature.mypage.R.string.mypage_settings

enum class MyPageMenu(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
) {
    SALES(mypage_sales_history, ic_sales_36),
    PURCHASE(mypage_purchase_history, ic_purchase_36),
    RECENT(mypage_recently_viewed, ic_recent_36),
    FAVORITE(mypage_favorites, ic_favorite_36),
    SETTINGS(mypage_settings, ic_settings_36),
    HELP(mypage_help_center, ic_help_36)
}