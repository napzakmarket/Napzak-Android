package com.napzak.market.report.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun ReportContactSection(
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
            text = stringResource(report_input_title_contact),
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.height(16.dp))

        NapzakDefaultTextField(
            text = reportState.contact,
            onTextChange = { reportState.contact = it },
            hint = stringResource(report_input_place_holder_contact),
            textStyle = NapzakMarketTheme.typography.caption12m,
            textColor = NapzakMarketTheme.colors.gray500,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            hintTextColor = NapzakMarketTheme.colors.gray200,
            isSingleLined = true,
            modifier = Modifier
                .onFocusEvent(focusEvent)
                .clip(RoundedCornerShape(14.dp))
                .background(NapzakMarketTheme.colors.gray50)
                .padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportDetailSectionPreview() {
    NapzakMarketTheme {
        val reportState = rememberReportState(ReportType.PRODUCT.toString())
        Column(Modifier.padding(horizontal = 20.dp)) {
            ReportContactSection(
                reportState = reportState,
                onTextFieldFocus = {},
            )
        }
    }
}
