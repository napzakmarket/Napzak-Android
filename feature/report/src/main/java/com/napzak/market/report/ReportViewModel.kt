package com.napzak.market.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var isUploading by mutableStateOf(false)
        private set

    private val id = savedStateHandle.get<Long>(ID)

    fun sendReport(
        reportType: ReportType,
        reason: String,
        detail: String,
        contact: String,
    ) = viewModelScope.launch {
        if (id == null) return@launch

        isUploading = true
        val reportParameters = ReportParameters(
            title = reason,
            description = detail,
            contact = contact,
        )

        runCatching {
            sendReport(reportType, id, reportParameters)
        }.onSuccess {
            _sideEffect.send(ReportSideEffect.ReportCompleted)
        }.onFailure { t ->
            Timber.e(t)
        }.also {
            isUploading = false
        }
    }

    private suspend fun sendReport(
        reportType: ReportType,
        id: Long,
        reportParameters: ReportParameters,
    ) {
        when (reportType) {
            ReportType.PRODUCT -> sendProductReport(
                productId = id,
                reportParameters = reportParameters,
            )

            ReportType.USER -> sendUserReport(
                userId = id,
                reportParameters = reportParameters,
            )
        }
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
