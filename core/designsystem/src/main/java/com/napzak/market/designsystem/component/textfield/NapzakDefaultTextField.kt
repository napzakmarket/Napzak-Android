package com.napzak.market.designsystem.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.theme.NapzakMarketTheme


/**
 * Napzak default text field
 *
 * @param text: 입력 값
 * @param onTextChange: 입력 값 변경
 * @param hint: 힌트
 * @param isSingleLined: single / multi line
 */
@Composable
fun NapzakDefaultTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    textStyle: TextStyle,
    hintTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    textColor: Color = NapzakMarketTheme.colors.gray500,
    hintTextColor: Color = NapzakMarketTheme.colors.gray200,
    isSingleLined: Boolean = true,
    suffix: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentAlignment: Alignment = Alignment.CenterStart,
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = textStyle.copy(color = textColor),
        singleLine = isSingleLined,
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = verticalAlignment,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = contentAlignment,
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            style = hintTextStyle.copy(color = hintTextColor),
                        )
                    }
                    innerTextField()
                }
                suffix?.invoke()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NapzakDefaultTextFieldPreview() {
    NapzakMarketTheme {
        var text by remember { mutableStateOf("") }
        NapzakDefaultTextField(
            text = text,
            onTextChange = { text = it },
            hint = "이름을 입력해주세요",
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12sb,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
