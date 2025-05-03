package com.napzak.market.report.mapper

import com.napzak.market.report.dto.ReportRequest
import com.napzak.market.report.model.ReportParameters

fun ReportParameters.toRequest() = with(this) {
    ReportRequest(
        title = title,
        description = description,
        contact = contact,
    )
}
