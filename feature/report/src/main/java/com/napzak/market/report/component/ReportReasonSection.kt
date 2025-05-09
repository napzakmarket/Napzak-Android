package com.napzak.market.report.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.report.R.string.report_input_title_reason
import com.napzak.market.report.state.ReportState
import com.napzak.market.report.state.rememberReportState
import com.napzak.market.report.type.ReportType
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ReportReasonSection(
    reportState: ReportState,
    dropdownEnabled: Boolean,
    onDropdownClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(report_input_title_reason),
        style = NapzakMarketTheme.typography.body14sb,
        color = NapzakMarketTheme.colors.gray500,
        modifier = modifier,
    )

    Spacer(modifier = modifier.height(16.dp))

    ReportReasonDropDownMenu(
        reasons = reportState.reasons.toImmutableList(),
        selectedReason = reportState.reason,
        onReasonSelect = { reportState.reason = it },
        onDropdownClick = onDropdownClick,
        dropdownEnabled = dropdownEnabled,
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
private fun ReportReasonDropDownMenu(
    @StringRes reasons: ImmutableList<Int>,
    @StringRes selectedReason: Int,
    dropdownEnabled: Boolean,
    onDropdownClick: (Boolean) -> Unit,
    onReasonSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    val borderModifier = Modifier.border(
        width = Dp.Hairline,
        color = NapzakMarketTheme.colors.gray200,
        shape = shape,
    )

    Column(
        modifier = modifier.then(borderModifier),
    ) {
        TextField(
            value = stringResource(selectedReason),
            onValueChange = { },
            textStyle = NapzakMarketTheme.typography.caption12sb,
            shape = shape,
            colors = dropDownMenuColor(),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(ic_down_chevron),
                    contentDescription = null,
                )
            },
            modifier = borderModifier
                .fillMaxWidth()
                .noRippleClickable { onDropdownClick(!dropdownEnabled) },
            readOnly = true,
            enabled = false,
        )

        AnimatedVisibility(
            visible = dropdownEnabled,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                reasons.forEach { reason ->
                    DropdownMenuItem(
                        text = stringResource(reason),
                        isSelected = reason == selectedReason,
                        onClick = {
                            onReasonSelect(reason)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = NapzakMarketTheme.colors
    val color = if (isSelected) colorScheme.purple500 else colorScheme.gray300
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            style = NapzakMarketTheme.typography.caption12m
                .copy(color = color),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun dropDownMenuColor(): TextFieldColors = TextFieldDefaults.colors(
    disabledTextColor = NapzakMarketTheme.colors.gray300,
    disabledTrailingIconColor = NapzakMarketTheme.colors.gray300,
    disabledContainerColor = NapzakMarketTheme.colors.white,
    disabledIndicatorColor = NapzakMarketTheme.colors.white,
)

@Preview(showBackground = true, heightDp = 300)
@Composable
private fun ReportReasonSectionPreview() {
    NapzakMarketTheme {
        val reportState = rememberReportState(ReportType.PRODUCT.toString())

        Column(modifier = Modifier.padding(20.dp)) {
            ReportReasonSection(
                reportState = reportState,
                dropdownEnabled = false,
                onDropdownClick = {},
            )
        }
    }
}
