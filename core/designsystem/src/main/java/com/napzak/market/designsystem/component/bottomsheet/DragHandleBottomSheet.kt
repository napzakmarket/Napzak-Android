package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsControllerCompat
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.designsystem.R
import com.napzak.market.ui_util.disableContrastEnforcement

/**
 * dragHandle 바텀 시트
 *
 * @param sheetState BottomSheet 상태
 * @param onDismissRequest 하향 드래그 또는 BottomSheet 외의 영역 클릭 시 실행됨
 * @param modifier 수정자 (bottomSheet 높이 지정 가능)
 * @param bottomSheetContent content 내용 (composable로 작성)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragHandleBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    bottomSheetContent: @Composable ColumnScope.() -> Unit,
) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = NapzakMarketTheme.colors.white,
        scrimColor = NapzakMarketTheme.colors.transBlack,
        dragHandle = {
            Column {
                Spacer(Modifier.height(17.dp))
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(2.dp)
                        .background(
                            color = NapzakMarketTheme.colors.gray100,
                            shape = RoundedCornerShape(32),
                        ),
                )
            }
        },
    ) {
        val view = LocalView.current
        val window = (view.parent as DialogWindowProvider).window

        SideEffect {
            window.disableContrastEnforcement()
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            WindowInsetsControllerCompat(window, view).isAppearanceLightNavigationBars = true
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 44.dp),
        ) {
            bottomSheetContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProductReportBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    NapzakMarketTheme {
        DragHandleBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { },
            modifier = Modifier.height(140.dp),
        ) {
            BottomSheetMenuItem(
                menuIcon = ImageVector.vectorResource(R.drawable.ic_error_24),
                menuName = "마켓 신고하기",
                onItemClick = { },
                textColor = NapzakMarketTheme.colors.red,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProductBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    NapzakMarketTheme {
        DragHandleBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { },
            modifier = Modifier.height(380.dp),
        ) {
            BottomSheetMenuItem(
                menuIcon = ImageVector.vectorResource(R.drawable.ic_edit_24),
                menuName = "상품 수정",
                onItemClick = { },
            )

            Spacer(Modifier.height(20.dp))

            BottomSheetMenuItem(
                menuIcon = ImageVector.vectorResource(R.drawable.ic_setting_24),
                menuName = "상품 상태 변경",
                onItemClick = { },
                isToggleOption = true,
                isToggleOpen = false,
            )

            Spacer(Modifier.height(20.dp))

            BottomSheetMenuItem(
                menuIcon = ImageVector.vectorResource(R.drawable.ic_delete_24),
                menuName = "삭제하기",
                onItemClick = { },
                textColor = NapzakMarketTheme.colors.red,
            )
        }
    }
}
