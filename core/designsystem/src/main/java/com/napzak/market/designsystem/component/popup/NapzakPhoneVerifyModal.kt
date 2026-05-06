package com.napzak.market.designsystem.component.popup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.napzak.market.designsystem.R.drawable.ic_phone_verification
import com.napzak.market.designsystem.R.string.phone_verification_modal_button
import com.napzak.market.designsystem.R.string.phone_verification_modal_content
import com.napzak.market.designsystem.R.string.phone_verification_modal_title

@Composable
fun NapzakPhoneVerifyModal(
    modifier: Modifier = Modifier,
    onDismissClick: () -> Unit,
    onPhoneVerifyClick: () -> Unit,
) {
    NapzakModal(
        title = stringResource(phone_verification_modal_title),
        content = stringResource(phone_verification_modal_content),
        image = ic_phone_verification,
        buttonText = stringResource(phone_verification_modal_button),
        onDismissRequest = onDismissClick,
        onButtonClick = onPhoneVerifyClick,
        modifier = modifier,
    )
}
