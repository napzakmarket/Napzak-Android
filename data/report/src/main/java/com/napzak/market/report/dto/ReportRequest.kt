package com.napzak.market.report.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    @SerialName("reportTitle") val title: String,
    @SerialName("reportDescription") val description: String,
    @SerialName("reportContact") val contact: String,
)
