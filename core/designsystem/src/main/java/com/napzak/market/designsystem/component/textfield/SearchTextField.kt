package com.napzak.market.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_cancel_search_12
import com.napzak.market.designsystem.R.drawable.ic_search_12
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

/**
 * Search text field
 *
 * @param text: 검색 입력 값
 * @param onTextChange: 검색 입력 값 변경
 * @param hint: 힌트
 * @param onSearchClick: 검색 버튼 / 키보드 검색 클릭
 * @param onResetClick: 검색 입력 값 초기화
 */
@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    onResetClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        NapzakDefaultTextField(
            text = text,
            onTextChange = onTextChange,
            hint = hint,
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            modifier = Modifier
                .focusRequester(focusRequester)
                .clip(RoundedCornerShape(14.dp))
                .background(NapzakMarketTheme.colors.gray50)
                .padding(16.dp, 13.dp, 0.dp, 13.dp),
            enabled = enabled,
            readOnly = readOnly,
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClick() },
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            suffix = {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(start = 10.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (text.isNotEmpty() && !readOnly) {
                        Icon(
                            imageVector = ImageVector.vectorResource(ic_cancel_search_12),
                            contentDescription = null,
                            modifier = Modifier
                                .noRippleClickable(onResetClick),
                            tint = Color.Unspecified,
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(ic_search_12),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .noRippleClickable(onSearchClick),
                        tint = Color.Unspecified,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    NapzakMarketTheme {
        var searchText by remember { mutableStateOf("") }
        SearchTextField(
            text = searchText,
            onTextChange = { searchText = it },
            hint = "어떤 상품을 찾고 계신가요?",
            onResetClick = { searchText = "" },
            modifier = Modifier.fillMaxWidth(),
            onSearchClick = {}
        )
    }
}
