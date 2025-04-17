package com.napzak.market.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.InputTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.report.R.string.report_input_letter_num_detail
import com.napzak.market.feature.report.R.string.report_input_place_holder_detail
import com.napzak.market.feature.report.R.string.report_input_title_detail
import com.napzak.market.report.state.ReportState
import com.napzak.market.report.state.rememberReportState
import com.napzak.market.report.type.ReportType

private val MinHeight = 180.dp
private const val DETAIL_LENGTH_MAX = 200

@Composable
internal fun ReportDetailSection(
    reportState: ReportState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(report_input_title_detail),
        style = NapzakMarketTheme.typography.body14sb,
        color = NapzakMarketTheme.colors.gray500,
        modifier = modifier,
    )

    Spacer(modifier.height(16.dp))

    InputTextField(
        text = reportState.detail,
        onTextChange = reportState::onDetailChange,
        hint = stringResource(report_input_place_holder_detail),
        modifier = modifier.defaultMinSize(minHeight = MinHeight),
        textStyle = NapzakMarketTheme.typography.caption12sb,
        textColor = NapzakMarketTheme.colors.gray400,
        hintTextStyle = NapzakMarketTheme.typography.caption12m,
        borderColor = NapzakMarketTheme.colors.gray200,
        isSingleLined = false,
        contentAlignment = Alignment.TopStart,
        paddingValues = PaddingValues(16.dp),
    )

    Spacer(modifier.height(9.dp))

    Text(
        text = stringResource(
            report_input_letter_num_detail,
            reportState.detail.length,
            DETAIL_LENGTH_MAX
        ),
        style = NapzakMarketTheme.typography.caption10sb.copy(
            color = NapzakMarketTheme.colors.gray300,
            textAlign = TextAlign.End,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
private fun ReportDetailSectionPreview() {
    NapzakMarketTheme {
        val reportState = rememberReportState(ReportType.USER.toString())

        Column {
            ReportDetailSection(
                reportState = reportState,
            )
        }
    }
}
