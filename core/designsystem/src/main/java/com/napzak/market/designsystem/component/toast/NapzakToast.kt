package com.napzak.market.designsystem.component.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.napzak.market.designsystem.theme.NapzakMarketTheme

val LocalNapzakToast = staticCompositionLocalOf<NapzakToast> {
    error("no NapzakToast instance provided")
}

@Immutable
class NapzakToast(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) : Toast(context) {
    /**
     * 납작마켓의 커스텀 토스트 메시지를 출력합니다.
     *
     * @param toastType 토스트 메시지의 종류를 나타냅니다.
     * @param message 토스트 메시지의 내용입니다.
     * @param icon 토스트 메시지의 아이콘입니다.
     * @param fontType SMALL은 caption12m, LARGE는 body14sb로 폰트를 설정합니다.
     * @param [yOffset]을 통해 화면 하단과 토스트 메시지간 간격을 설정할 수 있습니다.
     */
    fun makeText(
        toastType: ToastType,
        message: String,
        icon: Int? = null,
        fontType: ToastFontType = ToastFontType.SMALL,
        yOffset: Int = 0,
    ) {
        when (toastType) {
            ToastType.COMMON -> showCommonToast(
                message = message,
                icon = icon,
                fontType = fontType,
                yOffset = yOffset,
            )

            ToastType.HEART -> showHeartToast(
                message = message,
                yOffset = yOffset,
            )

            ToastType.WARNING -> showWarningToast(
                message = message,
                yOffset = yOffset,
            )
        }
    }

    private fun showCommonToast(
        message: String,
        icon: Int?,
        fontType: ToastFontType,
        yOffset: Int,
    ) = setView(yOffset = yOffset) {
        val textStyle = with(NapzakMarketTheme.typography) {
            when (fontType) {
                ToastFontType.SMALL -> caption12m.copy(
                    textAlign = TextAlign.Center,
                )

                ToastFontType.LARGE -> body14sb.copy(
                    textAlign = TextAlign.Center,
                )
            }
        }
        CommonSnackBar(
            message = message,
            icon = icon?.let { ImageVector.vectorResource(icon) },
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

    private fun showHeartToast(
        message: String,
        yOffset: Int,
    ) = setView(yOffset = yOffset) {
        HeartClickSnackBar(
            message = message,
        )
    }

    private fun showWarningToast(
        message: String,
        yOffset: Int,
    ) = setView(yOffset = yOffset) {
        WarningSnackBar(message = message)
    }

    private fun setView(
        yOffset: Int = 0,
        content: @Composable () -> Unit,
    ) {
        val views = ComposeView(context)

        views.setContent {
            NapzakMarketTheme {
                content()
            }
        }

        views.setViewTreeLifecycleOwner(lifecycleOwner)
        views.setViewTreeSavedStateRegistryOwner(lifecycleOwner as? SavedStateRegistryOwner)
        views.setViewTreeViewModelStoreOwner(lifecycleOwner as? ViewModelStoreOwner)

        this.duration = LENGTH_SHORT
        this.view = views

        this.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, yOffset)
        this.show()
    }

    fun toastOffsetWithBottomBar(): Int {
        return with(Density(context)) { 100.dp.toPx() }.toInt()
    }
}

