package com.napzak.market.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
 * @param suffix
 */
@Composable
fun InputTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = BLANK,
    textStyle: TextStyle = NapzakMarketTheme.typography.body14sb,
    textColor: Color = NapzakMarketTheme.colors.gray500,
    hintTextStyle: TextStyle = NapzakMarketTheme.typography.body14sb,
    hintTextColor: Color = NapzakMarketTheme.colors.gray200,
    borderColor: Color = NapzakMarketTheme.colors.gray100,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isSingleLined: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    suffix: @Composable (() -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues(14.dp, 16.dp, 14.dp, 16.dp),
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Start,
) {
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
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        hintTextStyle = hintTextStyle.copy(color = hintTextColor),
        isSingleLined = isSingleLined,
        visualTransformation = visualTransformation,
        suffix = suffix,
        verticalAlignment = Alignment.Top,
        contentAlignment = contentAlignment,
    )
}

@Preview
@Composable
private fun TitleInputTexFieldPreview() {
    NapzakMarketTheme {
        InputTextField(
            text = "",
            onTextChange = { },
            hint = "정확한 상품명을 포함하면 거래 확률이 올라가요",
        )
    }
}

@Preview
@Composable
private fun ContentInputTexFieldPreview() {
    NapzakMarketTheme {
        InputTextField(
            text = "",
            onTextChange = { },
            hint = "자세히 작성하면 더 빠르고 원활한 거래를 할 수 있어요\n" +
                    "예) 상품 상태, 한정판 여부, 네고 가능 여부 등",
            modifier = Modifier.height(136.dp),
            contentAlignment = Alignment.TopStart,
        )
    }
}

@Preview
@Composable
private fun PriceInputTexFieldPreview() {
    NapzakMarketTheme {
        InputTextField(
            text = "",
            onTextChange = { },
            hint = BLANK,
            isSingleLined = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            contentAlignment = Alignment.CenterEnd,
            suffix = {
                Text(
                    text = "원",
                    style = NapzakMarketTheme.typography.body14r,
                    modifier = Modifier.padding(start = 4.dp, end = 14.dp)
                )
            },
            paddingValues = PaddingValues(14.dp, 16.dp, 0.dp, 16.dp),
            textAlign = TextAlign.End,
        )
    }
}

@Preview
@Composable
private fun ReportTextFieldPreview() {
    NapzakMarketTheme {
        InputTextField(
            text = "",
            onTextChange = { },
            hint = "어떤 일이 있었나요?\n\n" +
                    "자세한 설명일수록 빠른 해결에 도움이 됩니다.\n" +
                    "신고 내용은 비공개로 안전하게 처리되니 안심하세요.\n" +
                    "안전한 거래 공간을 함께 만들어가요!",
            modifier = Modifier.height(180.dp),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            textColor = NapzakMarketTheme.colors.gray400,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            borderColor = NapzakMarketTheme.colors.gray200,
            isSingleLined = false,
            contentAlignment = Alignment.TopStart,
            paddingValues = PaddingValues(16.dp)
        )
    }
}