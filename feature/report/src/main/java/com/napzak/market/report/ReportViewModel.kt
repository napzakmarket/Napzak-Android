package com.napzak.market.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.type.ReportType
import com.napzak.market.navigation.keys.ReportScreenKey
import com.napzak.market.navigation.util.AssistedNavKeyFactory
import com.napzak.market.report.model.ReportParameters
import com.napzak.market.report.repository.ReportRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = ReportViewModel.Factory::class)
internal class ReportViewModel @AssistedInject constructor(
    @Assisted val navKey: ReportScreenKey,
    private val reportRepository: ReportRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory : AssistedNavKeyFactory<ReportViewModel, ReportScreenKey>

    private val _sideEffect = Channel<ReportSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    var isUploading by mutableStateOf(false)
        private set

    private val id = navKey.id

    fun sendReport(
        reportType: ReportType,
        reason: String,
        detail: String,
        contact: String,
    ) = viewModelScope.launch {
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

}
