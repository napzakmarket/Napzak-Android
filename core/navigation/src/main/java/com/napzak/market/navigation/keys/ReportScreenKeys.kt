package com.napzak.market.navigation.keys

import com.napzak.market.common.type.ReportType
import kotlinx.serialization.Serializable

@Serializable
data class ReportScreenKey(
    val reportType: ReportType,
    val id: Long,
) : ScreenKey
