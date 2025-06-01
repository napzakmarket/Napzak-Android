package com.napzak.market.mypage.withdraw

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.sign_out_confirm_button_confirm
import com.napzak.market.feature.mypage.R.string.sign_out_confirm_button_dismiss
import com.napzak.market.feature.mypage.R.string.sign_out_confirm_description
import com.napzak.market.feature.mypage.R.string.sign_out_confirm_title
import com.napzak.market.feature.mypage.R.string.sign_out_dialog_title
import com.napzak.market.feature.mypage.R.string.sign_out_top_bar
import com.napzak.market.ui_util.ScreenPreview

@Composable
internal fun WithdrawConfirmScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onNavigateUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val innerScreenScrollState = rememberScrollState()
    var withdrawDialogVisible by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            withdrawDialogVisible = false
        }
    }

    Scaffold(
        topBar = {
            NavigateUpTopBar(
                title = stringResource(sign_out_top_bar),
                onNavigateUp = onNavigateUpClick,
            )
        },
        bottomBar = {
            WithdrawConfirmBottomBar(
                onConfirmClick = { withdrawDialogVisible = true },
                onDismissClick = onCancelClick,
            )
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
                text = stringResource(sign_out_confirm_title),
                style = NapzakMarketTheme.typography.title20b,
                color = NapzakMarketTheme.colors.gray500,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = stringResource(sign_out_confirm_description),
                style = NapzakMarketTheme.typography.caption12r,
                color = NapzakMarketTheme.colors.black,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }

    AnimatedVisibility(
        visible = withdrawDialogVisible,
    ) {
        NapzakDialog(
            title = stringResource(sign_out_dialog_title),
            onConfirmClick = onConfirmClick,
            onDismissClick = { withdrawDialogVisible = false },
        )
    }
}

@Composable
private fun WithdrawConfirmBottomBar(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(14.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 5.dp, bottom = 20.dp),
    ) {
        val buttonModifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 50.dp)

        Button(
            onClick = onDismissClick,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = NapzakMarketTheme.colors.purple100,
                contentColor = NapzakMarketTheme.colors.purple500,
            ),
            modifier = buttonModifier,
        ) {
            Text(
                text = stringResource(sign_out_confirm_button_dismiss),
                style = NapzakMarketTheme.typography.caption12m,
            )
        }

        Button(
            onClick = onConfirmClick,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = NapzakMarketTheme.colors.purple500,
                contentColor = NapzakMarketTheme.colors.white,
            ),
            modifier = buttonModifier,
        ) {
            Text(
                text = stringResource(sign_out_confirm_button_confirm),
                style = NapzakMarketTheme.typography.caption12m,
            )
        }
    }
}

@ScreenPreview
@Composable
private fun WithdrawConfirmScreenPreview() {
    NapzakMarketTheme {
        WithdrawConfirmScreen(
            onCancelClick = {},
            onConfirmClick = {},
            onNavigateUpClick = {},
        )
    }
}
