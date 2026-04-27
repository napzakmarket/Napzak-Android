package com.napzak.market.mypage.withdraw

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.designsystem.component.scaffold.LocalInnerPadding
import com.napzak.market.mypage.withdraw.component.WithdrawConfirmScreen
import com.napzak.market.mypage.withdraw.component.WithdrawDetailScreen
import com.napzak.market.mypage.withdraw.component.WithdrawReasonScreen

private enum class WithdrawStep { REASON, DETAIL, CONFIRM }

@Composable
internal fun WithdrawRoute(
    onNavigateUp: () -> Unit,
    onWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WithdrawViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var step by remember { mutableStateOf(WithdrawStep.REASON) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val innerPaddingValues = LocalInnerPadding.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            if (it is WithdrawSideEffect.WithdrawComplete) onWithdrawComplete()
        }
    }

    BackHandler(true) {
        when (step) {
            WithdrawStep.REASON -> onNavigateUp()
            WithdrawStep.DETAIL -> step = WithdrawStep.REASON
            WithdrawStep.CONFIRM -> step = WithdrawStep.DETAIL
        }
    }

    when (step) {
        WithdrawStep.REASON -> WithdrawReasonScreen(
            reason = uiState.reason,
            onReasonSelect = viewModel::setReason,
            onProceedClick = { step = WithdrawStep.DETAIL },
            onNavigateUpClick = onNavigateUp,
            modifier = modifier.padding(innerPaddingValues),
        )

        WithdrawStep.DETAIL -> WithdrawDetailScreen(
            description = uiState.description,
            onDescriptionChange = viewModel::setDescription,
            onProceedClick = { step = WithdrawStep.CONFIRM },
            onNavigateUpClick = { step = WithdrawStep.REASON },
            modifier = modifier.padding(innerPaddingValues),
        )

        WithdrawStep.CONFIRM -> WithdrawConfirmScreen(
            isWithdrawing = uiState.isWithdrawing,
            onConfirmClick = {
                viewModel.deletePushToken()
                viewModel.withdrawStore()
            },
            onCancelClick = { step = WithdrawStep.REASON },
            onNavigateUpClick = { step = WithdrawStep.DETAIL },
            modifier = modifier.padding(innerPaddingValues),
        )
    }
}
