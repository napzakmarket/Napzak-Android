package com.napzak.market.detail.component.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_error_24
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetMenuItem
import com.napzak.market.designsystem.component.bottomsheet.DragHandleBottomSheet
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_bottom_sheet_report

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductBottomSheet(
    onReportClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    DragHandleBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .padding(bottom = 28.dp),
    ) {
        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(ic_error_24),
            menuName = stringResource(detail_bottom_sheet_report),
            onItemClick = onReportClick,
            textColor = NapzakMarketTheme.colors.red,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductBottomSheetPreview() {
    NapzakMarketTheme {
        ProductBottomSheet(
            onReportClick = { },
            onDismissRequest = { },
            modifier = Modifier.wrapContentHeight(),
        )
    }
}
