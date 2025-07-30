package com.napzak.market.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.register
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow

@Composable
internal fun BoxScope.RegistrationButton(
    onRegisterClick: () -> Unit,
    checkButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.transWhite,
                endColor = NapzakMarketTheme.colors.shadowBlack,
                direction = ShadowDirection.Top,
            )
            .background(NapzakMarketTheme.colors.white)
            .then(modifier),
    ) {
        NapzakButton(
            text = stringResource(register),
            onClick = onRegisterClick,
            enabled = checkButtonEnabled,
            modifier = Modifier
                .padding(top = 18.dp),
        )
    }
}
