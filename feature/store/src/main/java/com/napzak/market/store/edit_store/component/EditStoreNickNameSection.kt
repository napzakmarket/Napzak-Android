package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_button_name_check
import com.napzak.market.feature.store.R.string.store_edit_hint_name
import com.napzak.market.feature.store.R.string.store_edit_sub_title_name
import com.napzak.market.feature.store.R.string.store_edit_title_name
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.util.android.noRippleClickable

/**
 * 마켓의 이름을 수정하는 입력 필드를 제공하는 컴포넌트
 */
@Composable
internal fun EditStoreNickNameSection(
    marketName: String,
    onNameChange: (String) -> Unit,
    checkEnabled: Boolean,
    nickNameValidationState: NicknameValidationResult,
    nickNameDuplicationState: NicknameValidationResult,
    onNameValidityCheckClick: () -> Unit,
) {
    val nickNameCheckEvent = { if (checkEnabled) onNameValidityCheckClick() }

    val buttonColor = with(NapzakMarketTheme.colors) {
        if (checkEnabled) purple500
        else gray200
    }

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_name),
        subtitle = stringResource(store_edit_sub_title_name),
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 5.dp),
        ) {
            NapzakDefaultTextField(
                text = marketName,
                onTextChange = onNameChange,
                hint = stringResource(store_edit_hint_name),
                textStyle = NapzakMarketTheme.typography.caption12sb,
                hintTextStyle = NapzakMarketTheme.typography.caption12m,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        NapzakMarketTheme.colors.gray50,
                        RoundedCornerShape(14.dp),
                    )
                    .padding(PaddingValues(16.dp, 10.dp, 10.dp, 10.dp)),
                suffix = {
                    Box(
                        modifier = Modifier
                            .noRippleClickable(nickNameCheckEvent)
                            .background(buttonColor, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(store_edit_button_name_check),
                            style = NapzakMarketTheme.typography.caption12sb.copy(
                                color = NapzakMarketTheme.colors.gray50,
                            ),
                        )
                    }
                }
            )

            NickNameSupportingText(
                nickNameValidationState = nickNameValidationState,
                nickNameDuplicationState = nickNameDuplicationState,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun NickNameSupportingText(
    nickNameValidationState: NicknameValidationResult,
    nickNameDuplicationState: NicknameValidationResult,
    modifier: Modifier = Modifier,
) {

    val (supportingText, supportingTextColor) = when {
        nickNameDuplicationState is NicknameValidationResult.Valid ->
            "\u2022 ${nickNameDuplicationState.message}" to NapzakMarketTheme.colors.green

        nickNameDuplicationState is NicknameValidationResult.Invalid ->
            "\u2022 ${nickNameDuplicationState.errorMessage}" to NapzakMarketTheme.colors.red

        nickNameValidationState is NicknameValidationResult.Invalid ->
            "\u2022 ${nickNameValidationState.errorMessage}" to NapzakMarketTheme.colors.red

        else -> "" to NapzakMarketTheme.colors.gray300
    }

    Text(
        text = supportingText,
        color = supportingTextColor,
        style = NapzakMarketTheme.typography.caption12sb,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun EditStoreNickNameSectionPreview() {
    NapzakMarketTheme {
        Column {
            EditStoreNickNameSection(
                marketName = "",
                onNameChange = {},
                checkEnabled = false,
                nickNameValidationState = NicknameValidationResult.Empty,
                nickNameDuplicationState = NicknameValidationResult.Empty,
                onNameValidityCheckClick = {},
            )
        }
    }
}