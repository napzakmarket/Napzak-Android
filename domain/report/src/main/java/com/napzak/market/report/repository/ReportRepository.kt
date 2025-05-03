package com.napzak.market.report.repository

import com.napzak.market.report.model.ReportParameters

interface ReportRepository {
    suspend fun sendProductReport(
        productId: Long,
        reportParameters: ReportParameters,
    ): Result<Unit>

    suspend fun sendStoreReport(
        storeId: Long,
        reportParameters: ReportParameters,
    ): Result<Unit>
}