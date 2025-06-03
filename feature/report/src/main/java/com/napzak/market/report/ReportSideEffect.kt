package com.napzak.market.report

sealed interface ReportSideEffect {
    data class ShowToast(val message: String) : ReportSideEffect
    data object NavigateUp : ReportSideEffect
}