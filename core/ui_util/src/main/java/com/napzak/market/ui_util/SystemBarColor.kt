package com.napzak.market.ui_util

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import javax.inject.Inject

val LocalSystemBarsColor = staticCompositionLocalOf<SystemBarsColorController> {
    error("No SystemBarsColor provided")
}

class SystemBarsColorController @Inject constructor() {
    private var localSystemBarsColor by mutableStateOf(SystemBarsColor())

    fun setSystemBarColor(
        statusBarColor: Color,
        navigationBarColor: Color = Color.White,
        isStatusDarkIcon: Boolean = true,
        isNavigationDarkIcon: Boolean = true,
    ) {
        localSystemBarsColor = localSystemBarsColor.copy(
            statusBarColor = statusBarColor,
            navigationBarColor = navigationBarColor,
            isStatusDarkIcon = isStatusDarkIcon,
            isNavigationDarkIcon = isNavigationDarkIcon
        )
    }

    @Composable
    fun Apply(activity: Activity) {
        val window = activity.window
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        with(localSystemBarsColor) {
            controller.isAppearanceLightStatusBars = isStatusDarkIcon
            controller.isAppearanceLightNavigationBars = isNavigationDarkIcon
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = navigationBarColor.toArgb()
        }

        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusBarHeight)
                    .zIndex(1f)
                    .background(color = localSystemBarsColor.statusBarColor)
            )
        }
    }
}

@Immutable
private data class SystemBarsColor(
    val statusBarColor: Color = Color.White,
    val navigationBarColor: Color = Color.White,
    val isStatusDarkIcon: Boolean = true,
    val isNavigationDarkIcon: Boolean = true,
)