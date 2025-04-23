package com.napzak.market.onboarding.termsAgreement.model

enum class TermType(
    val title: String,
    val isRequired: Boolean,
    val detailContentUrl: String,
) {
    SERVICE_TERMS("이용약관", true,"https://understood-soldier-501.notion.site/1d1f54d645db80008565d662fbe6dd65"),
    PRIVACY_POLICY("개인정보처리방침", true,"https://understood-soldier-501.notion.site/1d1f54d645db80b48ce4ed8303b7a8be"),
}

fun TermType.getDisplayLabel(): String {
    return if (isRequired) "(필수) $title" else "(선택) $title"
}