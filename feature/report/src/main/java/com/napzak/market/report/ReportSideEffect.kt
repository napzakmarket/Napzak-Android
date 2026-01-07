package com.napzak.market.report

sealed interface ReportSideEffect {
    data object NavigateUp : ReportSideEffect
    data object ReportCompleted : ReportSideEffect
}