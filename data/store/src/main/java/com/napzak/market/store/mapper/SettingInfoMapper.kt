package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.SettingInfoResponse
import com.napzak.market.store.model.SettingInfo

fun SettingInfoResponse.toDomain(): SettingInfo = SettingInfo(
    noticeLink = noticeLink,
    termsLink = termsLink,
    privacyPolicyLink = privacyPolicyLink,
    versionInfoLink = versionInfoLink,
)