package com.napzak.market.store.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingInfoResponse(
    @SerialName("noticeLink") val noticeLink: String,
    @SerialName("termsLink") val termsLink: String,
    @SerialName("privacyPolicyLink") val privacyPolicyLink: String,
    @SerialName("versionInfoLink") val versionInfoLink: String
)