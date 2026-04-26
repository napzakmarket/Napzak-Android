package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data class ReportScreenKey(
    val reportType: String,
    val id: Long,
) : ScreenKey
