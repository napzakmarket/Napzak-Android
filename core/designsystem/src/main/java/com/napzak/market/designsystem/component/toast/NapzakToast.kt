package com.napzak.market.designsystem.component.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
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

class NapzakToast(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) : Toast(context) {

    fun showCommonToast(
        message: String,
        icon: Int? = null,
        fontType: NapzakToastFontType = NapzakToastFontType.LARGE,
    ) = makeText {
        val textStyle = with(NapzakMarketTheme.typography) {
            when (fontType) {
                NapzakToastFontType.SMALL -> caption12m.copy(
                    textAlign = TextAlign.Center,
                )

                NapzakToastFontType.LARGE -> body14sb.copy(
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

    fun showHeartToast(
        message: String,
        yOffset: Int = 100,
    ) = makeText(yOffset = yOffset) {
        HeartClickSnackBar(
            message = message,
        )
    }

    fun showWarningToast(
        message: String,
    ) = makeText {
        WarningSnackBar(message = message)
    }

    private fun makeText(
        yOffset: Int = 100,
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