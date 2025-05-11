package com.napzak.market.store.model

data class TermsAgreement(
    val bundleId: Int,
    val termList: List<Terms>,
)

data class Terms(
    val termsId: Long,
    val termsTitle: String,
    val termsUrl: String,
    val isRequired: Boolean,
)
