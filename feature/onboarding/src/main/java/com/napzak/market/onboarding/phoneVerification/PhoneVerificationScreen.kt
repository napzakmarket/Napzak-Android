package com.napzak.market.onboarding.phoneVerification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.designsystem.R.drawable.ic_circle_check
import com.napzak.market.designsystem.R.drawable.ic_indicator_second_step
import com.napzak.market.designsystem.component.button.NapzakBorderButton
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.button.NapzakCheckedButton
import com.napzak.market.designsystem.component.textfield.NapzakAffixTextField
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.onboarding.R.string.onboarding_next
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_agreement_age_over_14
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_name_edit_hint
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_number_edit_hint
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_prefix_korean
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_resend
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_sub_title
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_title
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_verification_check_button
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_verification_edit_hint
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_verification_success
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_verify
import com.napzak.market.feature.onboarding.R.string.onboarding_phone_verify_number
import com.napzak.market.onboarding.genre.component.OnboardingTopBar
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationUiState
import com.napzak.market.onboarding.phoneVerification.util.toTimeFormat

@Composable
internal fun PhoneVerificationRoute(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: PhoneVerificationViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val isOnboarding = viewModel.isOnboarding.collectAsStateWithLifecycle().value

    PhoneVerificationScreen(
        uiState = uiState,
        isOnboarding = isOnboarding,
        onNameChanged = viewModel::onNameChanged,
        onPhoneChanged = viewModel::onPhoneChanged,
        onCodeChanged = viewModel::onCodeChanged,
        onSendButtonClick = viewModel::requestVerification,
        onCodeCheckClick = viewModel::verifyCode,
        onAgeCheckedChange = viewModel::updateAgeChecked,
        onBackClick = onBackClick,
        onNextClick = {
            if (isOnboarding) onNextClick()
            else onBackClick()
        },
    )
}

@Composable
fun PhoneVerificationScreen(
    uiState: PhoneVerificationUiState,
    isOnboarding: Boolean,
    onNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onCodeChanged: (String) -> Unit,
    onSendButtonClick: () -> Unit,
    onCodeCheckClick: () -> Unit,
    onAgeCheckedChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
            .background(NapzakMarketTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 60.dp)
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ),
    ) {
        if (isOnboarding) {
            OnboardingTopBar(
                onBackClick = onBackClick,
                indicatorIcon = ic_indicator_second_step,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = stringResource(onboarding_phone_title),
            style = NapzakMarketTheme.typography.title20b,
            color = NapzakMarketTheme.colors.gray400,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(onboarding_phone_sub_title),
            style = NapzakMarketTheme.typography.caption12r,
            color = NapzakMarketTheme.colors.gray300,
        )

        Spacer(Modifier.height(30.dp))

        NapzakDefaultTextField(
            text = uiState.name,
            onTextChange = onNameChanged,
            hint = stringResource(onboarding_phone_name_edit_hint),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            textColor = NapzakMarketTheme.colors.gray500,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp)
                )
                .padding(PaddingValues(16.dp, 18.dp, 10.dp, 18.dp)),
        )

        Spacer(Modifier.height(12.dp))

        NapzakAffixTextField(
            text = uiState.phone,
            onTextChange = onPhoneChanged,
            hint = stringResource(onboarding_phone_number_edit_hint),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            textColor = NapzakMarketTheme.colors.gray500,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp)
                )
                .padding(PaddingValues(16.dp, 10.dp, 10.dp, 10.dp)),
            prefix = {
                Text(
                    text = stringResource(onboarding_phone_prefix_korean),
                    style = NapzakMarketTheme.typography.caption12sb,
                    color = NapzakMarketTheme.colors.gray500,
                    modifier = Modifier.padding(end = 10.dp),
                )
            },
            suffix = {
                with(uiState) {
                    val buttonText =
                        if (isSend) stringResource(onboarding_phone_resend)
                        else stringResource(onboarding_phone_verify)

                    Box(
                        modifier = Modifier
                            .background(
                                color =
                                    if (isSendEnabled) NapzakMarketTheme.colors.purple500
                                    else NapzakMarketTheme.colors.gray200,
                                shape = RoundedCornerShape(10.dp),
                            )
                            .clickable(enabled = isSendEnabled) {
                                onSendButtonClick()
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = buttonText,
                            style = NapzakMarketTheme.typography.caption12sb,
                            color = NapzakMarketTheme.colors.white,
                        )
                    }
                }
            },
        )

        if (uiState.isSend) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(onboarding_phone_verify_number),
                style = NapzakMarketTheme.typography.caption12r,
                color = NapzakMarketTheme.colors.gray300,
            )

            Spacer(Modifier.height(12.dp))

            NapzakAffixTextField(
                text = uiState.code,
                onTextChange = onCodeChanged,
                hint = stringResource(onboarding_phone_verification_edit_hint),
                textStyle = NapzakMarketTheme.typography.caption12sb,
                hintTextStyle = NapzakMarketTheme.typography.caption12m,
                textColor = NapzakMarketTheme.colors.gray500,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        NapzakMarketTheme.colors.gray50,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(PaddingValues(16.dp, 10.dp, 10.dp, 10.dp)),
                prefix = {
                    if (uiState.isVerificationSuccess) {
                        Icon(
                            imageVector = ImageVector.vectorResource(ic_circle_check),
                            contentDescription = null,
                            tint = NapzakMarketTheme.colors.green,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                    }
                },
                suffix = {
                    with(uiState) {
                        Box(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = remainingTimeSec.toTimeFormat(),
                                style = NapzakMarketTheme.typography.caption12sb,
                                color =
                                    if (remainingTimeSec == 0) NapzakMarketTheme.colors.gray300
                                    else NapzakMarketTheme.colors.purple500,
                            )
                        }
                    }
                },
            )

            Spacer(Modifier.height(12.dp))

            NapzakBorderButton(
                text =
                    if (uiState.isVerificationSuccess)
                        stringResource(onboarding_phone_verification_success)
                    else
                        stringResource(onboarding_phone_verification_check_button),
                enabled = uiState.isVerifyEnabled,
                onClick = {
                    onCodeCheckClick()
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        NapzakCheckedButton(
            checked = uiState.isAgeChecked,
            onCheckedChange = onAgeCheckedChange,
            text = stringResource(onboarding_phone_agreement_age_over_14),
        )

        NapzakButton(
            text = stringResource(onboarding_next),
            onClick = onNextClick,
            enabled = uiState.isNextEnabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneVerificationScreenPreview() {
    NapzakMarketTheme {
        PhoneVerificationScreen(
            uiState = PhoneVerificationUiState(),
            isOnboarding = true,
            onNameChanged = {},
            onPhoneChanged = {},
            onCodeChanged = {},
            onSendButtonClick = {},
            onCodeCheckClick = {},
            onAgeCheckedChange = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}
