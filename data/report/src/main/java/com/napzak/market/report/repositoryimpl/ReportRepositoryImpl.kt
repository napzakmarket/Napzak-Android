package com.napzak.market.report.repositoryimpl

import com.napzak.market.report.datasource.ReportDataSource
import com.napzak.market.report.mapper.toRequest
import com.napzak.market.report.model.ReportParameters
import com.napzak.market.report.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportDataSource: ReportDataSource
) : ReportRepository {
    override suspend fun sendProductReport(
        productId: Long,
        reportParameters: ReportParameters
    ): Result<Unit> = runCatching {
        reportDataSource.sendProductReport(
            productId = productId,
            request = reportParameters.toRequest()
        )
    }

    override suspend fun sendStoreReport(
        storeId: Long,
        reportParameters: ReportParameters
    ): Result<Unit> = runCatching {
        reportDataSource.sendStoreReport(
            storeId = storeId,
            request = reportParameters.toRequest()
        )
    }
}
