package com.napzak.market.designsystem.component.toast;

import android.widget.Toast
import com.napzak.market.designsystem.component.toast.ToastType.COMMON
import com.napzak.market.designsystem.component.toast.ToastType.HEART
import com.napzak.market.designsystem.component.toast.ToastType.WARNING

/**
 * [Toast]의 종류를 나타냅니다.
 *
 * @property COMMON [CommonSnackBar] 디자인의 토스트 메시지를 보여줍니다.
 * @property HEART [HeartClickSnackBar] 디자인의 토스트 메시지를 보여줍니다.
 * @property WARNING [WarningSnackBar] 디자인의 토스트 메시지를 보여줍니다.
 */
enum class ToastType {
    COMMON, HEART, WARNING
}

enum class ToastFontType {
    SMALL, LARGE;
}