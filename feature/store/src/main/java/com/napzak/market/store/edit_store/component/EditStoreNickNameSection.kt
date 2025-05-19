package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_button_name_check
import com.napzak.market.feature.store.R.string.store_edit_error_empty
import com.napzak.market.feature.store.R.string.store_edit_error_only_consonants
import com.napzak.market.feature.store.R.string.store_edit_error_only_numbers
import com.napzak.market.feature.store.R.string.store_edit_error_special_char
import com.napzak.market.feature.store.R.string.store_edit_error_whitespace
import com.napzak.market.feature.store.R.string.store_edit_hint_name
import com.napzak.market.feature.store.R.string.store_edit_sub_title_name
import com.napzak.market.feature.store.R.string.store_edit_title_name
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.model.NicknameValidationResult.Error.CONTAINS_CONSONANT
import com.napzak.market.store.model.NicknameValidationResult.Error.EMPTY
import com.napzak.market.store.model.NicknameValidationResult.Error.ONLY_CONSONANTS
import com.napzak.market.store.model.NicknameValidationResult.Error.ONLY_NUMBERS
import com.napzak.market.store.model.NicknameValidationResult.Error.SPECIAL_CHAR
import com.napzak.market.store.model.NicknameValidationResult.Error.WHITESPACE
import com.napzak.market.util.android.noRippleClickable

private const val NICKNAME_MAX_LENGTH = 20

@Composable
internal fun EditStoreNickNameSection(
    marketName: String,
    onNameChange: (String) -> Unit,
    checkEnabled: Boolean,
    nickNameValidationState: NicknameValidationResult,
    nickNameDuplicationState: UiState<String>,
    onNameValidityCheckClick: () -> Unit,
) {
    EditStoreProfileContainer(
        title = stringResource(store_edit_title_name),
        subtitle = stringResource(store_edit_sub_title_name),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
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
                        color = NapzakMarketTheme.colors.gray50,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .padding(start = 16.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                suffix = {
                    NickNameCheckButton(
                        enabled = checkEnabled,
                        onClick = onNameValidityCheckClick,
                    )
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
            ) {
                NickNameSupportingText(
                    nickNameValidationState = nickNameValidationState,
                    nickNameDuplicationState = nickNameDuplicationState,
                )

                NicknameLengthText(nickname = marketName)
            }
        }
    }
}

@Composable
private fun NickNameCheckButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nickNameCheckEvent = { if (enabled) onClick() }
    val buttonColor = with(NapzakMarketTheme.colors) {
        if (enabled) purple500
        else gray200
    }

    Box(
        modifier = modifier
            .noRippleClickable(nickNameCheckEvent)
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(10.dp),
            )
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

@Composable
private fun NickNameSupportingText(
    nickNameValidationState: NicknameValidationResult,
    nickNameDuplicationState: UiState<String>,
    modifier: Modifier = Modifier,
) {
    val bulletedText = "\u2022 %s"

    val (supportingText, supportingTextColor) = when {
        nickNameDuplicationState is UiState.Success ->
            bulletedText.format(nickNameDuplicationState.data) to NapzakMarketTheme.colors.green

        nickNameDuplicationState is UiState.Failure ->
            bulletedText.format(nickNameDuplicationState.msg) to NapzakMarketTheme.colors.red

        nickNameValidationState is NicknameValidationResult.Invalid ->
            bulletedText.format(nickNameValidationState.error.toMessage()) to NapzakMarketTheme.colors.red

        else -> "" to NapzakMarketTheme.colors.gray300
    }

    Text(
        text = supportingText,
        color = supportingTextColor,
        style = NapzakMarketTheme.typography.caption12sb,
        modifier = modifier,
    )
}

@Composable
private fun NicknameLengthText(
    nickname: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "${nickname.length}/$NICKNAME_MAX_LENGTH",
            style = NapzakMarketTheme.typography.caption10r.copy(
                color = NapzakMarketTheme.colors.gray300,
            ),
        )
    }
}

@Composable
fun NicknameValidationResult.Error.toMessage(): String {
    val stringRes = when (this) {
        EMPTY -> store_edit_error_empty
        WHITESPACE -> store_edit_error_whitespace
        SPECIAL_CHAR -> store_edit_error_special_char
        ONLY_NUMBERS -> store_edit_error_only_numbers
        ONLY_CONSONANTS -> store_edit_error_only_consonants
        CONTAINS_CONSONANT -> store_edit_error_only_consonants
    }
    return stringResource(stringRes)
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
                nickNameValidationState = NicknameValidationResult.Valid,
                nickNameDuplicationState = UiState.Success("사용 가능한 이름이에요!"),
                onNameValidityCheckClick = {},
            )
        }
    }
}
