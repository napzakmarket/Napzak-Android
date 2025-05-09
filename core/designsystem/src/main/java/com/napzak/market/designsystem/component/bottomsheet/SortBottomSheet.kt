package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.R.drawable.ic_check_purple
import com.napzak.market.designsystem.R.drawable.ic_x_button_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable
import kotlinx.coroutines.launch

/**
 * 정렬 BottomSheet
 *
 * @param selectedSortType 선택된 정렬값
 * @param sheetState BottomSheet 상태
 * @param onDismissRequest x 버튼 또는 BottomSheet 외의 영역 클릭 시 실행됨
 * @param onSortItemClick 정렬 아이템 클릭 시 실행됨
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    selectedSortType: SortType,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onSortItemClick: (SortType) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) { sheetState.expand() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 31.dp, topEnd = 31.dp),
        containerColor = NapzakMarketTheme.colors.white,
        scrimColor = NapzakMarketTheme.colors.transBlack,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 36.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_x_button_24),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .noRippleClickable {
                        coroutineScope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                onDismissRequest()
                            }
                    },
            )

            Spacer(Modifier.height(2.dp))

            SortType.entries.forEach { sortItem ->
                SortItem(
                    sortType = sortItem.label,
                    isSelected = sortItem == selectedSortType,
                    onSortItemClick = {
                        coroutineScope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                onSortItemClick(sortItem)
                            }
                    },
                )
            }
        }
    }
}

@Composable
private fun SortItem(
    sortType: String,
    isSelected: Boolean,
    onSortItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onSortItemClick)
            .padding(horizontal = 10.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = sortType,
            style = NapzakMarketTheme.typography.body14b,
            color = if (isSelected) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray300,
        )

        if (isSelected) {
            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = ImageVector.vectorResource(ic_check_purple),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SortBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    NapzakMarketTheme {
        SortBottomSheet(
            selectedSortType = SortType.RECENT,
            sheetState = sheetState,
            onDismissRequest = { },
            onSortItemClick = { },
        )
    }
}