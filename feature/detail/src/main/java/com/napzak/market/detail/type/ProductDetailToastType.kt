package com.napzak.market.detail.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.ic_check_snackbar_18
import com.napzak.market.designsystem.R.drawable.ic_delete_snackbar_16
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.toast.ToastFontType
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.feature.detail.R.string.detail_toast_trade_status

enum class ProductDetailToastType(
    val toastType: ToastType,
    val fontType: ToastFontType,
    @StringRes val stringRes: Int,
    @DrawableRes val iconRes: Int?,
) {
    DELETE(
        toastType = ToastType.COMMON,
        fontType = ToastFontType.LARGE,
        stringRes = detail_toast_trade_status,
        iconRes = ic_delete_snackbar_16,
    ),
    STATUS_CHANGE(
        toastType = ToastType.COMMON,
        fontType = ToastFontType.LARGE,
        stringRes = detail_toast_trade_status,
        iconRes = ic_check_snackbar_18
    ),
    LIKE(
        toastType = ToastType.HEART,
        fontType = ToastFontType.SMALL,
        stringRes = heart_click_snackbar_message,
        iconRes = null
    )
}
