package com.napzak.market.designsystem.component.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
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
        duration: Int = LENGTH_SHORT,
    ) = makeText(
        message = message,
        icon = icon,
        toastType = NapzakToastType.COMMON,
        duration = duration,
    )

    fun showHeartToast(
        message: String,
        yOffset: Int = 100,
    ) = makeText(
        message = message,
        toastType = NapzakToastType.HEART,
        yOffset = yOffset
    )

    fun showWarningToast(
        message: String,
    ) = makeText(
        message = message,
        toastType = NapzakToastType.WARNING,
    )

    private fun makeText(
        message: String,
        icon: Int? = null,
        toastType: NapzakToastType,
        duration: Int = LENGTH_SHORT,
        yOffset: Int = 100,
    ) {
        val views = ComposeView(context)

        views.setContent {
            NapzakMarketTheme {
                when (toastType) {
                    NapzakToastType.COMMON -> CommonSnackBar(
                        message = message,
                        icon = icon?.let { ImageVector.vectorResource(icon) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )

                    NapzakToastType.HEART -> HeartClickSnackBar(
                        message = message,
                    )

                    NapzakToastType.WARNING -> WarningSnackBar(message = message)
                }
            }
        }

        views.setViewTreeLifecycleOwner(lifecycleOwner)
        views.setViewTreeSavedStateRegistryOwner(lifecycleOwner as? SavedStateRegistryOwner)
        views.setViewTreeViewModelStoreOwner(lifecycleOwner as? ViewModelStoreOwner)

        this.duration = duration
        this.view = views

        this.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, yOffset)
        this.show()
    }

    fun toastOffsetWithBottomBar(): Int {
        return with(Density(context)) { 100.dp.toPx() }.toInt()
    }
}