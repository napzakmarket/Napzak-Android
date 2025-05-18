package com.napzak.market.report.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.napzak.market.feature.report.R.string.report_product_banned_product
import com.napzak.market.feature.report.R.string.report_product_exaggerated_advertisement
import com.napzak.market.feature.report.R.string.report_product_mal_content
import com.napzak.market.feature.report.R.string.report_product_miscellaneous
import com.napzak.market.feature.report.R.string.report_product_skirmish
import com.napzak.market.feature.report.R.string.report_product_slang_used
import com.napzak.market.feature.report.R.string.report_product_title
import com.napzak.market.feature.report.R.string.report_user_bad_manner
import com.napzak.market.feature.report.R.string.report_user_fraud
import com.napzak.market.feature.report.R.string.report_user_miscellaneous
import com.napzak.market.feature.report.R.string.report_user_skirmish
import com.napzak.market.feature.report.R.string.report_user_slang_used
import com.napzak.market.feature.report.R.string.report_user_title
import com.napzak.market.report.type.ReportType

@Composable
internal fun rememberReportState(
    reportType: String
) = rememberSaveable(saver = ReportState.Saver, key = reportType) {
    ReportState(ReportType.valueOf(reportType))
}

@Stable
internal class ReportState(
    val reportType: ReportType,
) {
    @StringRes
    val reasons: List<Int> = getReasons(reportType)

    @StringRes
    val title: Int = getTitles(reportType)

    var reason by mutableIntStateOf(reasons.first())
    var detail by mutableStateOf("")
        private set
    var contact by mutableStateOf("")

    /**
     * 신고 제출 버튼의 활성화 여부를 판단하기 위한 Boolean 값
     */
    val isReportFilled: Boolean
        get() = detail.isNotBlank() && contact.isNotBlank()

    fun onDetailChange(value: String) {
        detail = value.take(200)
    }

    private fun getReasons(reportType: ReportType): List<Int> =
        when (reportType) {
            ReportType.PRODUCT -> listOf(
                report_product_banned_product,
                report_product_mal_content,
                report_product_exaggerated_advertisement,
                report_product_slang_used,
                report_product_skirmish,
                report_product_miscellaneous,
            )

            ReportType.USER -> listOf(
                report_user_bad_manner,
                report_user_fraud,
                report_user_skirmish,
                report_user_slang_used,
                report_user_miscellaneous,
            )
        }

    private fun getTitles(reportType: ReportType): Int = when (reportType) {
        ReportType.PRODUCT -> report_product_title
        ReportType.USER -> report_user_title
    }

    companion object {
        internal val Saver = Saver<ReportState, String>(
            save = { it.reportType.toString() },
            restore = { ReportState(ReportType.valueOf(it)) }
        )
    }
}