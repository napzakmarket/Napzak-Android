package com.napzak.market.store.store.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.component.bottomsheet.BottomSheetMenuItem
import com.napzak.market.designsystem.component.bottomsheet.DragHandleBottomSheet
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_report
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoreReportBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onReportButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) { sheetState.expand() }

    DragHandleBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier.height(140.dp),
    ) {
        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(R.drawable.ic_error_24),
            menuName = stringResource(store_report),
            onItemClick = {
                coroutineScope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion {
                        onReportButtonClick()
                    }
            },
            textColor = NapzakMarketTheme.colors.red,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun StoreBottomSheetPreview(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    NapzakMarketTheme {
        StoreReportBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            onReportButtonClick = {},
            modifier = modifier
        )
    }
}