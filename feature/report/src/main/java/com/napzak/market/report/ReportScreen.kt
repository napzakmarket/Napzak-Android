package com.napzak.market.report

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastFontType
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.report.R.drawable.ic_chevron_left
import com.napzak.market.feature.report.R.string.report_button_submit
import com.napzak.market.report.component.ReportContactSection
import com.napzak.market.report.component.ReportDetailSection
import com.napzak.market.report.component.ReportReasonSection
import com.napzak.market.report.state.ReportState
import com.napzak.market.report.state.rememberReportState
import com.napzak.market.report.type.ReportType
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ReportRoute(
    reportType: String,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val napzakToast = LocalNapzakToast.current

    val reportState = rememberReportState(reportType)

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    ReportSideEffect.NavigateUp -> {
                        navigateUp()
                    }

                    is ReportSideEffect.ShowToast -> {
                        napzakToast.makeText(
                            toastType = ToastType.COMMON,
                            message = sideEffect.message,
                            fontType = ToastFontType.SMALL,
                            yOffset = 50,
                        )
                    }
                }
            }
    }

    ReportScreen(
        reportState = reportState,
        onSubmitButtonClick = {
            viewModel.sendReport(
                reportType = reportState.reportType,
                reason = context.getString(reportState.reason),
                detail = reportState.detail,
                contact = reportState.contact,
            )
        },
        onNavigateUp = navigateUp,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReportScreen(
    reportState: ReportState,
    onSubmitButtonClick: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var dropdownEnabled by remember { mutableStateOf(false) }
    val disableDropDown = { dropdownEnabled = false }

    FocusSideEffectsHandler(
        dropdownEnabled = dropdownEnabled,
        disableDropDown = disableDropDown,
        onNavigateUp = onNavigateUp,
    )

    Scaffold(
        topBar = {
            ReportTopBar(
                onNavigateUp = onNavigateUp,
            )
        },
        bottomBar = {
            ReportSubmitButton(
                onClick = onSubmitButtonClick,
                enabled = reportState.isReportFilled,
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, _ ->
                        focusManager.clearFocus()
                        disableDropDown()
                    }
                }
        ) {
            Spacer(Modifier.height(40.dp))

            Text(
                text = stringResource(reportState.title),
                style = NapzakMarketTheme.typography.title20b.copy(
                    color = NapzakMarketTheme.colors.gray500
                ),
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(Modifier.height(30.dp))

            ReportReasonSection(
                reportState = reportState,
                dropdownEnabled = dropdownEnabled,
                onDropdownClick = { dropdownEnabled = it },
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            SectionDivider(Modifier.padding(vertical = 30.dp))

            ReportDetailSection(
                reportState = reportState,
                onTextFieldFocus = disableDropDown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            SectionDivider(Modifier.padding(top = 9.dp, bottom = 30.dp))

            ReportContactSection(
                reportState = reportState,
                onTextFieldFocus = disableDropDown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(Modifier.height(35.dp))
        }
    }
}

@Composable
private fun ReportTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 1.dp,
        color = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(contentAlignment = Alignment.TopStart) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_chevron_left),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier
                    .noRippleClickable(onNavigateUp)
                    .wrapContentWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
            )
        }
    }
}

@Composable
private fun ReportSubmitButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 6.dp),
    ) {
        NapzakButton(
            text = stringResource(report_button_submit),
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SectionDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        thickness = 4.dp,
        color = NapzakMarketTheme.colors.gray10,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FocusSideEffectsHandler(
    dropdownEnabled: Boolean,
    disableDropDown: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.isImeVisible

    // 드롭다운 메뉴가 열리면 포커스 제거
    LaunchedEffect(dropdownEnabled) {
        if (dropdownEnabled) focusManager.clearFocus()
    }

    // 키보드가 내려가면 포커스 제거
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus(force = true)
        }
    }

    // 드롭다운 메뉴가 열려있으면 백버튼으로 닫음
    BackHandler {
        if (dropdownEnabled) disableDropDown()
        else onNavigateUp()
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportScreenPreview() {
    NapzakMarketTheme {
        val reportType = ReportType.USER
        val reportState = rememberReportState(reportType.toString())

        ReportScreen(
            reportState = reportState,
            onSubmitButtonClick = { },
            onNavigateUp = { },
        )
    }
}
