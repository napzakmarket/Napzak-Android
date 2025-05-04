package com.napzak.market.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.report.model.ReportParameters
import com.napzak.market.report.repository.ReportRepository
import com.napzak.market.report.type.ReportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reportRepository: ReportRepository,
) : ViewModel() {
    private val _sideEffect = Channel<ReportSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val id = savedStateHandle.get<Long>(ID)

    fun sendReport(
        reportType: ReportType,
        reason: String,
        detail: String,
        contact: String,
    ) = viewModelScope.launch {
        runCatching {
            val reportParameters = ReportParameters(
                title = reason,
                description = detail,
                contact = contact,
            )

            when (reportType) {
                ReportType.PRODUCT -> sendProductReport(
                    productId = id!!,
                    reportParameters = reportParameters,
                )

                ReportType.USER -> sendUserReport(
                    userId = id!!,
                    reportParameters = reportParameters,
                )
            }
        }.onSuccess {
            Timber.tag("ReportViewModel")
                .d("checking if this message appears at the end of the log")
            _sideEffect.send(ReportSideEffect.NavigateUp)
        }.onFailure(Timber::e)
    }

    private suspend fun sendProductReport(
        productId: Long,
        reportParameters: ReportParameters,
    ) {
        reportRepository.sendProductReport(
            productId = productId,
            reportParameters = reportParameters,
        )
    }

    private suspend fun sendUserReport(
        userId: Long,
        reportParameters: ReportParameters,
    ) {
        reportRepository.sendStoreReport(
            storeId = userId,
            reportParameters = reportParameters,
        )
    }

    companion object {
        private const val ID = "id"
    }
}
