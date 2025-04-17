package com.napzak.market.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        when (reportType) {
            ReportType.PRODUCT -> sendProductReport(
                reason = reason,
                detail = detail,
                contact = contact,
            )

            ReportType.USER -> sendUserReport(
                reason = reason,
                detail = detail,
                contact = contact,
            )
        }
    }

    private suspend fun sendProductReport(
        reason: String,
        detail: String,
        contact: String,
    ) {
        // TODO: 물품 신고 API 호출
        Timber.tag("ReportViewModel")
            .d("sendProductReport called with reason: $reason, detail: $detail, contact: $contact and id: $id")
    }

    private suspend fun sendUserReport(
        reason: String,
        detail: String,
        contact: String,
    ) {
        // TODO: 마켓 신고 API 호출
        Timber.tag("ReportViewModel")
            .d("sendUserReport called with reason: $reason, detail: $detail, contact: $contact and id: $id")
    }

    companion object {
        private const val ID = "id"
    }
}