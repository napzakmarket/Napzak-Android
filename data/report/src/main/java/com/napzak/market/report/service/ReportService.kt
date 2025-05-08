package com.napzak.market.report.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.report.dto.ReportProductResponse
import com.napzak.market.report.dto.ReportRequest
import com.napzak.market.report.dto.ReportStoreResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportService {
    @POST("products/report/{productId}")
    suspend fun postProductReport(
        @Path("productId") productId: Long,
        @Body request: ReportRequest
    ): BaseResponse<ReportProductResponse>

    @POST("stores/report/{storeId}")
    suspend fun postStoreReport(
        @Path("storeId") storeId: Long,
        @Body request: ReportRequest
    ): BaseResponse<ReportStoreResponse>
}
