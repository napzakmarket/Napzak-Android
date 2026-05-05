package com.napzak.market.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_circle_check
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * Affix text field: prefix, suffix가 있는 텍스트필드
 *
 * @param text: 입력 값
 * @param onTextChange: 입력 값 변경
 * @param hint: 힌트
 * @param isSingleLined: single / multi line
 */
@Composable
fun NapzakAffixTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    textStyle: TextStyle,
    hintTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    textColor: Color = NapzakMarketTheme.colors.gray500,
    hintTextColor: Color = NapzakMarketTheme.colors.gray200,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isSingleLined: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Start,
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = textStyle.copy(
            color = textColor,
            textAlign = textAlign,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLined,
        visualTransformation = visualTransformation,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = verticalAlignment,
            ) {
                prefix?.invoke()

                Box(
                    modifier = Modifier.weight(1f),
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

/**
 * Affix text field: prefix, suffix가 있는 텍스트필드
 *
 * 일반 텍스트가 아닌 TextFieldValue를 값으로 사용합니다.
 * 포맷이 필요한 텍스트필드에서 사용됩니다.
 * ex) 전화번호 (숫자만 입력해도 형식에 맞춰 변경 000-0000-0000)
 *
 * @param value: 입력 값
 * @param onTextChange: 입력 값 변경
 * @param hint: 힌트
 * @param isSingleLined: single / multi line
 */
@Composable
fun NapzakAffixTextField(
    value: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    hint: String,
    textStyle: TextStyle,
    hintTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    textColor: Color = NapzakMarketTheme.colors.gray500,
    hintTextColor: Color = NapzakMarketTheme.colors.gray200,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isSingleLined: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Start,
) {
    BasicTextField(
        value = value,
        onValueChange = onTextChange,
        textStyle = textStyle.copy(
            color = textColor,
            textAlign = textAlign,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLined,
        visualTransformation = visualTransformation,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = verticalAlignment,
            ) {
                prefix?.invoke()

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = contentAlignment,
                ) {
                    if (value.text.isEmpty()) {
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

@Preview
@Composable
private fun NapzakAffixTextFieldPreview() {
    NapzakMarketTheme {
        NapzakAffixTextField(
            text = "010-1234-1234",
            onTextChange = {  },
            hint = "",
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            textColor = NapzakMarketTheme.colors.gray500,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp)
                )
                .padding(PaddingValues(16.dp, 10.dp, 10.dp, 10.dp)),
            prefix = {
                Icon(
                    imageVector = ImageVector.vectorResource(ic_circle_check),
                    contentDescription = null,
                    tint = NapzakMarketTheme.colors.green,
                    modifier = Modifier.padding(end = 4.dp),
                )
            },
            suffix = {
                Box(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "00:00",
                        style = NapzakMarketTheme.typography.caption12sb,
                        color = NapzakMarketTheme.colors.purple500,
                    )
                }
            },
        )
    }
}