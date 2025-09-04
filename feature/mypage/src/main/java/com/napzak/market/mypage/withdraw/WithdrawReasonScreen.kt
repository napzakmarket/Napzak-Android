package com.napzak.market.mypage.withdraw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.spinner.NapzakSpinner
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.sign_out_button_proceed
import com.napzak.market.feature.mypage.R.string.sign_out_reason_description
import com.napzak.market.feature.mypage.R.string.sign_out_reason_spinner_description
import com.napzak.market.feature.mypage.R.string.sign_out_reason_spinner_title
import com.napzak.market.feature.mypage.R.string.sign_out_reason_title
import com.napzak.market.feature.mypage.R.string.sign_out_top_bar
import com.napzak.market.mypage.withdraw.type.WithdrawReasonType
import com.napzak.market.ui_util.ScreenPreview
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun WithdrawReasonScreen(
    onReasonSelect: (String, Int) -> Unit,
    onProceedClick: () -> Unit,
    onNavigateUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val innerScreenScrollState = rememberScrollState()
    val withdrawReasons = remember { WithdrawReasonType.entries.map { it.reason } }
    val paddingModifier = Modifier.padding(horizontal = 20.dp)

    LaunchedEffect(Unit) {
        onReasonSelect(withdrawReasons.first(), 1)
    }

    Scaffold(
        topBar = {
            NavigateUpTopBar(
                title = stringResource(sign_out_top_bar),
                onNavigateUp = onNavigateUpClick,
            )
        },
        bottomBar = {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = paddingModifier.padding(top = 5.dp, bottom = 20.dp),
            ) {
                NapzakButton(
                    text = stringResource(sign_out_button_proceed),
                    onClick = onProceedClick,
                )
            }
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(state = innerScreenScrollState),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(sign_out_reason_title),
                style = NapzakMarketTheme.typography.title20b,
                color = NapzakMarketTheme.colors.gray500,
                modifier = paddingModifier,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(sign_out_reason_description),
                style = NapzakMarketTheme.typography.body14r,
                color = NapzakMarketTheme.colors.gray300,
                modifier = paddingModifier,
            )

            HorizontalDivider(
                color = NapzakMarketTheme.colors.gray10,
                thickness = 4.dp,
                modifier = Modifier.padding(
                    top = 45.dp,
                    bottom = 30.dp,
                )
            )

            Text(
                text = stringResource(sign_out_reason_spinner_title),
                style = NapzakMarketTheme.typography.title20b,
                color = NapzakMarketTheme.colors.gray500,
                modifier = paddingModifier,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(sign_out_reason_spinner_description),
                style = NapzakMarketTheme.typography.body14r,
                color = NapzakMarketTheme.colors.gray300,
                modifier = paddingModifier,
            )

            Spacer(Modifier.height(20.dp))

            NapzakSpinner(
                options = withdrawReasons.toImmutableList(),
                initialOption = withdrawReasons.first(),
                onOptionSelect = {
                    onReasonSelect(it, withdrawReasons.indexOf(it) + 1)
                },
                modifier = paddingModifier,
            )
        }
    }
}

@ScreenPreview
@Composable
private fun WithdrawReasonScreenPreview() {
    NapzakMarketTheme {
        var withdrawReason by remember { mutableStateOf("") }

        WithdrawReasonScreen(
            onReasonSelect = { it, _ -> withdrawReason = it },
            onProceedClick = {},
            onNavigateUpClick = {},
        )
    }
}
