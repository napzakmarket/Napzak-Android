package com.napzak.market.report.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportProductResponse(
    @SerialName("reporterId") val reporterId: Long,
    @SerialName("reportedProductId") val reportedStoreId: Long,
    @SerialName("reportTitle") val reportTitle: String,
    @SerialName("reportDescription") val reportDescription: String,
    @SerialName("reportContact") val reportContact: String,
)
