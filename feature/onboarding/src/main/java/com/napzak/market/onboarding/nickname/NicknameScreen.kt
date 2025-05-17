package com.napzak.market.onboarding.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_second_step_indicator
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.onboarding.R.string.onboarding_next
import com.napzak.market.feature.onboarding.R.string.onboarding_nickname_edit_hint
import com.napzak.market.feature.onboarding.R.string.onboarding_nickname_sub_title
import com.napzak.market.feature.onboarding.R.string.onboarding_nickname_success
import com.napzak.market.feature.onboarding.R.string.onboarding_nickname_title
import com.napzak.market.feature.onboarding.R.string.onboarding_nickname_validation_button
import com.napzak.market.onboarding.nickname.model.NicknameUiState
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun NicknameRoute(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: NicknameViewModel = hiltViewModel(),
) {
    NicknameScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onNicknameChanged = viewModel::onNicknameChanged,
        onCheckDuplicationClick = viewModel::checkNicknameDuplication,
        onBackClick = onBackClick,
        onNextClick = {
            viewModel.updateNickname(
                onSuccess = onNextClick,
                onError = {
                    //TODO 추후 구현
                }
            )
        },
    )
}

@Composable
fun NicknameScreen(
    uiState: NicknameUiState,
    onCheckDuplicationClick: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val isValidationPassed = uiState.validationResult is NicknameValidationResult.Valid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 40.dp)
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ),
    ) {
        NicknameTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(30.dp))

        Text(
            text = stringResource(onboarding_nickname_title),
            style = NapzakMarketTheme.typography.title20b,
            color = NapzakMarketTheme.colors.gray400,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(onboarding_nickname_sub_title),
            style = NapzakMarketTheme.typography.caption12r,
            color = NapzakMarketTheme.colors.gray300,
        )

        Spacer(Modifier.height(30.dp))

        NapzakDefaultTextField(
            text = uiState.nickname,
            onTextChange = onNicknameChanged,
            hint = stringResource(onboarding_nickname_edit_hint),
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
            suffix = {
                Box(
                    modifier = Modifier
                        .background(
                            if (isValidationPassed) NapzakMarketTheme.colors.purple500
                            else NapzakMarketTheme.colors.gray200,
                            RoundedCornerShape(10.dp),
                        )
                        .clickable(enabled = isValidationPassed) {
                            onCheckDuplicationClick()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(onboarding_nickname_validation_button),
                        style = NapzakMarketTheme.typography.caption12sb,
                        color = NapzakMarketTheme.colors.white,
                    )
                }
            },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NicknameValidationResultMessage(
                validationResult = uiState.validationResult,
                duplicationError = uiState.duplicationError,
                isAvailable = uiState.isAvailable,
                modifier = Modifier.align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${uiState.nickname.length}/20",
                style = NapzakMarketTheme.typography.caption12m,
                color = NapzakMarketTheme.colors.gray300,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

        NapzakButton(
            text = stringResource(onboarding_next),
            onClick = onNextClick,
            enabled = uiState.isNextEnabled,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun NicknameTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_arrow_left),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onBackClick)
                .size(12.dp),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_second_step_indicator),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun NicknameValidationResultMessage(
    validationResult: NicknameValidationResult,
    duplicationError: String?,
    isAvailable: Boolean?,
    modifier: Modifier,
) {
    when {
        duplicationError != null -> {
            ValidationText(
                message = duplicationError,
                color = NapzakMarketTheme.colors.red,
            )
        }

        isAvailable == true -> {
            ValidationText(
                message = stringResource(onboarding_nickname_success),
                color = NapzakMarketTheme.colors.green,
            )
        }

        validationResult is NicknameValidationResult.Invalid -> {
            val error = validationResult as NicknameValidationResult.Invalid
            ValidationText(
                message = stringResource(id = error.error.toMessageRes()),
                color = NapzakMarketTheme.colors.red,
            )
        }

        else -> {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
private fun ValidationText(
    message: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp),
    ) {
        Text(
            text = "·",
            style = NapzakMarketTheme.typography.caption12sb,
            color = color,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = message,
            style = NapzakMarketTheme.typography.caption12sb,
            color = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NicknameScreenPreview() {
    NapzakMarketTheme {
        NicknameScreen(
            uiState = NicknameUiState(
                nickname = "연진",
                validationResult = NicknameValidationResult.Valid,
            ),
            onNicknameChanged = {},
            onCheckDuplicationClick = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}