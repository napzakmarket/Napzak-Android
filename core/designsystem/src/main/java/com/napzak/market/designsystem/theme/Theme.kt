package com.napzak.market.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
    primary = purple500,
    background = white
)

private val LocalNapzakMarketColors = staticCompositionLocalOf<NapzakMarketColors> {
    error("No NapzakColors provided")
}

private val LocalNapzakMarketTypography = staticCompositionLocalOf<NapzakMarketTypography> {
    error("No NapzakTypography provided")
}

object NapzakMarketTheme {
    val colors: NapzakMarketColors
        @Composable
        @ReadOnlyComposable
        get() = LocalNapzakMarketColors.current
    val typography: NapzakMarketTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalNapzakMarketTypography.current
}

@Composable
fun ProvideNapzakColorsAndTypography(
    colors: NapzakMarketColors,
    typography: NapzakMarketTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }.apply { update(colors) }
    val provideTypography = remember { typography.copy() }.apply { update(typography) }
    CompositionLocalProvider(
        LocalNapzakMarketColors provides provideColors,
        LocalNapzakMarketTypography provides provideTypography,
        content = content
    )
}

@Composable
fun NapzakMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = NapzakMarketLightColors()
    val colorScheme = LightColorScheme
    val typography = NapzakMarketTypography()

    ProvideNapzakColorsAndTypography(
        colors = colors,
        typography = typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
