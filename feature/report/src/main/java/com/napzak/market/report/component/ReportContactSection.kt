package com.napzak.market.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.report.R.string.report_input_place_holder_contact
import com.napzak.market.feature.report.R.string.report_input_title_contact
import com.napzak.market.report.state.ReportState
import com.napzak.market.report.state.rememberReportState
import com.napzak.market.report.type.ReportType

@Composable
internal fun ReportContactSection(
    reportState: ReportState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(report_input_title_contact),
        style = NapzakMarketTheme.typography.body14sb,
        color = NapzakMarketTheme.colors.gray500,
        modifier = modifier,
    )

    Spacer(modifier = modifier.height(16.dp))

    NapzakDefaultTextField(
        text = reportState.contact,
        onTextChange = { reportState.contact = it },
        hint = stringResource(report_input_place_holder_contact),
        textStyle = NapzakMarketTheme.typography.caption12m,
        textColor = NapzakMarketTheme.colors.gray500,
        hintTextStyle = NapzakMarketTheme.typography.caption12m,
        hintTextColor = NapzakMarketTheme.colors.gray200,
        isSingleLined = true,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(NapzakMarketTheme.colors.gray50)
            .padding(16.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun ReportDetailSectionPreview() {
    NapzakMarketTheme {
        val reportState = rememberReportState(ReportType.PRODUCT.toString())
        Column(Modifier.padding(horizontal = 20.dp)) {
            ReportContactSection(
                reportState = reportState
            )
        }
    }
}
