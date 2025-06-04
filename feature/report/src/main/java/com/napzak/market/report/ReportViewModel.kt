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
            _sideEffect.send(ReportSideEffect.NavigateUp)
            _sideEffect.send(ReportSideEffect.ShowToast(REPORT_SNACK_BAR_MESSAGE))
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
        private const val REPORT_SNACK_BAR_MESSAGE =
            "소중한 신고 감사합니다! \uD83D\uDE4F\n\n" +
                    "신고 내용을 꼼꼼히 검토하여\n" +
                    "입력하신 연락처로 결과를 안내해드릴게요.\n" +
                    "추가 정보가 필요할 경우 동일한 연락처로 문의드릴 수 있어요."
    }
}
