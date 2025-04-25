package com.napzak.market.mypage.signout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.sign_out_button_proceed
import com.napzak.market.feature.mypage.R.string.sign_out_button_skip
import com.napzak.market.feature.mypage.R.string.sign_out_detail_hint
import com.napzak.market.feature.mypage.R.string.sign_out_detail_title
import com.napzak.market.feature.mypage.R.string.sign_out_top_bar
import com.napzak.market.util.android.ScreenPreview
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun SignOutDetailScreen(
    onProceedClick: (String) -> Unit,
    onNavigateUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            NavigateUpTopBar(
                title = stringResource(sign_out_top_bar),
                onNavigateUp = onNavigateUpClick,
            )
        },
        bottomBar = {
            SignOutDetailBottomBar(
                onProceedClick = { onProceedClick(inputText) },
                onSkipClick = { onProceedClick("") },
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(sign_out_detail_title),
                style = NapzakMarketTheme.typography.title20b,
                color = NapzakMarketTheme.colors.gray500,
            )

            Spacer(Modifier.height(16.dp))

            NapzakDefaultTextField(
                text = inputText,
                onTextChange = { inputText = it },
                hint = stringResource(sign_out_detail_hint),
                textStyle = NapzakMarketTheme.typography.caption12r,
                textColor = NapzakMarketTheme.colors.gray500,
                hintTextStyle = NapzakMarketTheme.typography.caption12r,
                hintTextColor = NapzakMarketTheme.colors.gray200,
                isSingleLined = false,
                verticalAlignment = Alignment.Top,
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.77f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NapzakMarketTheme.colors.gray50)
                    .padding(12.dp),
            )
        }
    }
}

@Composable
private fun SignOutDetailBottomBar(
    onProceedClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 5.dp, bottom = 20.dp),
    ) {
        NapzakButton(
            text = stringResource(sign_out_button_proceed),
            onClick = onProceedClick,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(sign_out_button_skip),
            style = NapzakMarketTheme.typography.caption12m,
            color = NapzakMarketTheme.colors.gray300,
            modifier = Modifier.noRippleClickable(onSkipClick),
        )
    }
}

@ScreenPreview
@Composable
private fun SignOutReasonScreenPreview() {
    NapzakMarketTheme {
        SignOutDetailScreen(
            onProceedClick = {},
            onNavigateUpClick = {},
        )
    }
}
