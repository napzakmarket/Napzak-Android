package com.napzak.market.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_right
import com.napzak.market.designsystem.R.drawable.ic_logo
import com.napzak.market.designsystem.R.drawable.ic_purple_change
import com.napzak.market.designsystem.component.popup.NapzakPopup
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.splash.R.string.market_url
import com.napzak.market.feature.splash.R.string.update_popup_button
import com.napzak.market.feature.splash.R.string.update_popup_subtitle
import com.napzak.market.feature.splash.R.string.update_popup_title
import com.napzak.market.ui_util.getVersionName
import com.napzak.market.ui_util.openUrl
import kotlinx.coroutines.delay

@Composable
internal fun SplashRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val view = LocalView.current
    val context = LocalContext.current
    val packageName = context.packageName
    val marketUrl = stringResource(market_url, packageName)
    val isUpdatePopupVisible by viewModel.isUpdatePopupVisible.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        onDispose {
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }
    }

    val navigated = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val appVersion = context.getVersionName()
        if (appVersion != null) viewModel.checkAppVersion(appVersion) else { /* null인 경우 대기 */}
        delay(2500)
    }

    LaunchedEffect(isUpdatePopupVisible) {
        val success = viewModel.tryAutoLogin().isSuccess

        if (!navigated.value && isUpdatePopupVisible == false) {
            navigated.value = true
            if (success) onNavigateToMain() else onNavigateToLogin()
        }
    }

    SplashScreen(
        isUpdatePopupVisible = isUpdatePopupVisible,
        onUpdateButtonClick = { context.openUrl(marketUrl) },
        modifier = modifier,
    )
}

@Composable
private fun SplashScreen(
    isUpdatePopupVisible: Boolean?,
    onUpdateButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.purple500),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(ic_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1.7f),
        )
    }

    if (isUpdatePopupVisible == true) {
        UpdatePopup(
            onUpdateButtonClick = onUpdateButtonClick,
        )
    }
}

@Composable
private fun UpdatePopup(
    onUpdateButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
    ) {
        NapzakPopup(
            title = stringResource(update_popup_title),
            subTitle = stringResource(update_popup_subtitle),
            icon = ImageVector.vectorResource(ic_purple_change),
            buttonColor = NapzakMarketTheme.colors.purple500,
            buttonText = stringResource(update_popup_button),
            buttonIcon = ImageVector.vectorResource(ic_gray_arrow_right),
            onButtonClick = onUpdateButtonClick,
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    NapzakMarketTheme {
        SplashScreen(
            isUpdatePopupVisible = false,
            onUpdateButtonClick = {},
        )
    }
}
