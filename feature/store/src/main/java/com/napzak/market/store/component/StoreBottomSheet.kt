package com.napzak.market.store.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetMenuItem
import com.napzak.market.designsystem.component.bottomsheet.DragHandleBottomSheet
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoreBottomSheet(
    onReportButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    DragHandleBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { },
        modifier = modifier.height(140.dp),
    ) {
        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(R.drawable.ic_error_24),
            menuName = "마켓 신고하기",
            onItemClick = onReportButtonClick,
            textColor = NapzakMarketTheme.colors.red,
        )
    }
}

@Preview
@Composable
private fun StoreBottomSheetPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        StoreBottomSheet(
            onReportButtonClick = {},
            modifier = modifier
        )
    }
}