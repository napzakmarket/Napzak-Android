package com.napzak.market.mypage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.ic_sales_32
import com.napzak.market.designsystem.R.drawable.ic_purchase_32
import com.napzak.market.designsystem.R.drawable.ic_recent_30
import com.napzak.market.designsystem.R.drawable.ic_favorite_27
import com.napzak.market.designsystem.R.drawable.ic_settings_27
import com.napzak.market.designsystem.R.drawable.ic_help_32
import com.napzak.market.feature.mypage.R.string.mypage_sales_history
import com.napzak.market.feature.mypage.R.string.mypage_purchase_history
import com.napzak.market.feature.mypage.R.string.mypage_recently_viewed
import com.napzak.market.feature.mypage.R.string.mypage_favorites
import com.napzak.market.feature.mypage.R.string.mypage_settings
import com.napzak.market.feature.mypage.R.string.mypage_help_center

enum class MyPageMenu(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
) {
    SALES(mypage_sales_history, ic_sales_32),
    PURCHASE(mypage_purchase_history, ic_purchase_32),
    RECENT(mypage_recently_viewed, ic_recent_30),
    FAVORITE(mypage_favorites, ic_favorite_27),
    SETTINGS(mypage_settings, ic_settings_27),
    HELP(mypage_help_center, ic_help_32)
}