package com.napzak.market.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

private const val BLANK = ""

/**
 * Input text field
 *
 * @param text: 입력 값
 * @param onTextChange: 입력 값 변경
 * @param hint: 힌트
 * @param isSingleLined: single / multi line
 * @param isError
 * @param suffix
 */
@Composable
fun InputTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = BLANK,
    textStyle: TextStyle = NapzakMarketTheme.typography.body14r,
    hintTextStyle: TextStyle = NapzakMarketTheme.typography.body14sb,
    hintTextColor: Color = NapzakMarketTheme.colors.gray200,
    isSingleLined: Boolean = false,
    isError: Boolean = false,
    suffix: @Composable (() -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues(14.dp, 16.dp, 14.dp, 16.dp),
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Start
) {
    val borderColor = if (isError) NapzakMarketTheme.colors.red else NapzakMarketTheme.colors.gray100
    val textColor = if (isError) NapzakMarketTheme.colors.red else NapzakMarketTheme.colors.gray500

    NapzakDefaultTextField(
        text = text,
        onTextChange = onTextChange,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(NapzakMarketTheme.colors.white)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(paddingValues),
        hint = hint,
        textStyle = textStyle.copy(textAlign = textAlign),
        textColor = textColor,
        hintTextStyle = hintTextStyle.copy(color = hintTextColor),
        isSingleLined = isSingleLined,
        suffix = suffix,
        verticalAlignment = Alignment.Top,
        contentAlignment = contentAlignment,
    )
}

@Preview
@Composable
private fun InputTexFieldPreview() {
    NapzakMarketTheme {
        var text by remember { mutableStateOf("") }
        InputTextField(
            text = text,
            onTextChange = { text = it },
            hint = "정확한 상품명을 포함하면 거래 확률이 올라가요",
            modifier = Modifier.height(136.dp),
            isSingleLined = false,
            isError = text.length > 10,
        )
    }
}

@Preview
@Composable
private fun PriceInputTexFieldPreview() {
    NapzakMarketTheme {
        var text by remember { mutableStateOf("") }
        InputTextField(
            text = text,
            onTextChange = { text = it },
            hint = BLANK,
            isSingleLined = false,
            isError = text.length > 10,
            contentAlignment = Alignment.CenterEnd,
            suffix = {
                Text(
                    text = "원",
                    style = NapzakMarketTheme.typography.body14r.copy(color = NapzakMarketTheme.colors.gray200),
                    modifier = Modifier.padding(end = 14.dp)
                )
            },
            paddingValues = PaddingValues(14.dp, 16.dp, 0.dp, 16.dp),
        )
    }
}