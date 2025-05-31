package com.napzak.market.ui_util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackBarController(
    private val snackBarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    var imageRes by mutableStateOf<Int?>(null)
        private set

    fun show(message: String, imageRes: Int? = null) {
        this.imageRes = imageRes
        scope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }
}

val LocalSnackBarController = staticCompositionLocalOf<SnackBarController> {
    error("No SnackBarController provided")
}