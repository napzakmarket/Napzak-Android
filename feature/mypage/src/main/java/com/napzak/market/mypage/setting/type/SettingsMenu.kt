package com.napzak.market.mypage.setting.type

import androidx.annotation.StringRes
import com.napzak.market.feature.mypage.R.string.settings_item_notice_title
import com.napzak.market.feature.mypage.R.string.settings_item_privacy_policy_title
import com.napzak.market.feature.mypage.R.string.settings_item_terms_of_use_title
import com.napzak.market.feature.mypage.R.string.settings_item_version_info_title

enum class SettingsMenu(
    @StringRes val titleResId: Int,
) {
    NOTICE(settings_item_notice_title),
    TERMS_OF_USE(settings_item_terms_of_use_title),
    PRIVACY_POLICY(settings_item_privacy_policy_title),
    VERSION_INFO(settings_item_version_info_title);
}