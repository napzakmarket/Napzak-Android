package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermsResponse(
    @SerialName("bundleId") val bundleId: Int,
    @SerialName("termList") val termList: List<TermDto>,
)

@Serializable
data class TermDto(
    @SerialName("termsId") val termsId: Long,
    @SerialName("termsTitle") val termsTitle: String,
    @SerialName("termsUrl") val termsUrl: String,
    @SerialName("isRequired") val isRequired: Boolean,
)


