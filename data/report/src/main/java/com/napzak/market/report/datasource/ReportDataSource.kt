package com.napzak.market.report.datasource

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.report.dto.ReportProductResponse
import com.napzak.market.report.dto.ReportRequest
import com.napzak.market.report.dto.ReportStoreResponse
import com.napzak.market.report.service.ReportService
import javax.inject.Inject

class ReportDataSource @Inject constructor(
    private val reportService: ReportService,
) {
    suspend fun sendProductReport(
        productId: Long,
        request: ReportRequest,
    ): BaseResponse<ReportProductResponse> =
        reportService.postProductReport(
            productId = productId,
            request = request,
        )

    suspend fun sendStoreReport(
        storeId: Long,
        request: ReportRequest,
    ): BaseResponse<ReportStoreResponse> =
        reportService.postStoreReport(
            storeId = storeId,
            request = request,
        )
}
