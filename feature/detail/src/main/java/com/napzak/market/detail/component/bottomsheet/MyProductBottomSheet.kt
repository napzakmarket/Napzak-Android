package com.napzak.market.detail.component.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_delete_24
import com.napzak.market.designsystem.R.drawable.ic_edit_24
import com.napzak.market.designsystem.R.drawable.ic_setting_24
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetMenuItem
import com.napzak.market.designsystem.component.bottomsheet.DragHandleBottomSheet
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_delete
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_edit
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_radio_button_before_trade_buy
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_radio_button_before_trade_sell
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_radio_button_complete_trade_buy
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_radio_button_complete_trade_sell
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_radio_button_reserved
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_setting
import com.napzak.market.ui_util.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyProductBottomSheet(
    tradeType: TradeType,
    tradeStatus: TradeStatusType,
    onDismissRequest: () -> Unit,
    onModifyClick: () -> Unit,
    onStatusChange: (tradeStatus: TradeStatusType) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    var isToggleOpen by remember { mutableStateOf(false) }

    DragHandleBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier.height(380.dp),
    ) {
        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(ic_edit_24),
            menuName = stringResource(detail_bottom_sheet_edit),
            onItemClick = onModifyClick,
        )

        Spacer(Modifier.height(20.dp))

        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(ic_setting_24),
            menuName = stringResource(detail_bottom_sheet_setting),
            onItemClick = { isToggleOpen = !isToggleOpen },
            isToggleOption = true,
            isToggleOpen = isToggleOpen,
        )

        AnimatedVisibility(
            visible = isToggleOpen,
        ) {
            TradeStatusRadioButtonGroup(
                tradeType = tradeType,
                tradeStatus = tradeStatus,
                onStatusChange = { status ->
                    if (tradeStatus != status) {
                        onStatusChange(status)
                        onDismissRequest()
                    }
                }
            )
        }

        Spacer(Modifier.height(20.dp))

        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(ic_delete_24),
            menuName = stringResource(detail_bottom_sheet_delete),
            onItemClick = onDeleteClick,
            textColor = NapzakMarketTheme.colors.red,
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun TradeStatusRadioButtonGroup(
    tradeType: TradeType,
    tradeStatus: TradeStatusType,
    onStatusChange: (TradeStatusType) -> Unit,
) {
    val radioButtonGroup = rememberRadioButtonGroup(tradeType)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 30.dp, top = 16.dp),
    ) {
        radioButtonGroup.forEach { (status, textRes) ->
            val text = stringResource(textRes)
            TradeStatusRadioButton(
                text = text,
                isSelected = (tradeStatus == status),
                onClick = { onStatusChange(status) },
            )
        }
    }
}

@Composable
private fun TradeStatusRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (isSelected) NapzakMarketTheme.colors.purple500
    else NapzakMarketTheme.colors.gray100
    val textColor = if (isSelected) NapzakMarketTheme.colors.purple500
    else NapzakMarketTheme.colors.gray500

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics(mergeDescendants = true) { role = Role.RadioButton }
            .noRippleClickable(onClick),
    ) {
        Canvas(modifier = Modifier.size(14.dp)) {
            drawCircle(color = color, style = Stroke())
            drawCircle(color = color, style = Fill, radius = 4.dp.toPx())
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = text,
            style = NapzakMarketTheme.typography.body14sb,
            color = textColor,
        )
    }
}

@Composable
private fun rememberRadioButtonGroup(
    tradeType: TradeType,
) = remember(tradeType) {
    when (tradeType) {
        TradeType.BUY -> listOf(
            TradeStatusType.BEFORE_TRADE to detail_bottom_sheet_radio_button_before_trade_buy,
            TradeStatusType.RESERVED to detail_bottom_sheet_radio_button_reserved,
            TradeStatusType.COMPLETED_BUY to detail_bottom_sheet_radio_button_complete_trade_buy,
        )

        TradeType.SELL -> listOf(
            TradeStatusType.BEFORE_TRADE to detail_bottom_sheet_radio_button_before_trade_sell,
            TradeStatusType.RESERVED to detail_bottom_sheet_radio_button_reserved,
            TradeStatusType.COMPLETED_SELL to detail_bottom_sheet_radio_button_complete_trade_sell,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyProductBottomSheetPreview() {
    var tradeStatus by remember { mutableStateOf(TradeStatusType.BEFORE_TRADE) }

    NapzakMarketTheme {
        MyProductBottomSheet(
            tradeStatus = tradeStatus,
            tradeType = TradeType.SELL,
            onDismissRequest = { },
            onDeleteClick = { },
            onModifyClick = { },
            onStatusChange = { tradeStatus = it },
            modifier = Modifier.wrapContentHeight(),
        )
    }
}