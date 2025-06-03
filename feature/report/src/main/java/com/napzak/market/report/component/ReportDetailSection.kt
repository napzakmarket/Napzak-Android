package com.napzak.market.report.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val MinHeight = 180.dp
private const val DETAIL_LENGTH_MAX = 200

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReportDetailSection(
    reportState: ReportState,
    onTextFieldFocus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusEvent: (FocusState) -> Unit = remember {
        {
            val imeDelay = 300L
            if (it.isFocused) {
                onTextFieldFocus()
                coroutineScope.launch {
                    delay(imeDelay)
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester),
    ) {
        Text(
            text = stringResource(report_input_title_detail),
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(modifier.height(16.dp))

        InputTextField(
            text = reportState.detail,
            onTextChange = reportState::onDetailChange,
            hint = stringResource(report_input_place_holder_detail),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            textColor = NapzakMarketTheme.colors.gray400,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            borderColor = NapzakMarketTheme.colors.gray200,
            isSingleLined = false,
            contentAlignment = Alignment.TopStart,
            paddingValues = PaddingValues(16.dp),
            modifier = Modifier
                .onFocusEvent(focusEvent)
                .defaultMinSize(minHeight = MinHeight),
        )

        Spacer(Modifier.height(9.dp))

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
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ReportDetailSectionPreview() {
    NapzakMarketTheme {
        val reportState = rememberReportState(ReportType.USER.toString())

        Column {
            ReportDetailSection(
                reportState = reportState,
                onTextFieldFocus = {},
            )
        }
    }
}
