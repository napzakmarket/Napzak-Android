package com.napzak.market.report

sealed interface ReportSideEffect {
    data class ShowSnackBar(val message: String) : ReportSideEffect
    data object NavigateUp : ReportSideEffect
}