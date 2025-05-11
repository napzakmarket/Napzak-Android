package com.napzak.market.util.android

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackBarController(
    private val snackBarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    fun show(message: String) {
        scope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }
}

val LocalSnackBarController = staticCompositionLocalOf<SnackBarController> {
    error("No SnackBarController provided")
}