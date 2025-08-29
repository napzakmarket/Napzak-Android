package com.napzak.market.ui_util

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Material3의 기본 리플 모션을 제거하기 위한 함수입니다.
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = { onClick() },
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleClickable(enabled: Boolean, crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled,
            onClick = { onClick() },
        )
    }

/**
 * combineClick의 Ripple 효과를 제거하기 위한 함수입니다.
 */
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleCombineClickable(
    crossinline onClick: () -> Unit = {},
    crossinline onLongClick: () -> Unit,
): Modifier = composed {
    combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = { onClick() },
        onLongClick = { onLongClick() },
    )
}

/**
 * 커스텀 리플 효과를 넣기 위한 함수입니다.
 */
inline fun Modifier.napzakRippleClickable(
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    clickable(
        role = role,
        indication = ripple(false, 20.dp, Color.Unspecified),
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

/**
 * 스로트 처리 함수입니다
 */
inline fun Modifier.throttledNoRippleClickable(
    throttleTime: Long = 100L,
    coroutineScope: CoroutineScope,
    crossinline onClick: () -> Unit
): Modifier = composed {
    var isClickable by remember { mutableStateOf(true) }

    noRippleClickable {
        if (isClickable) {
            isClickable = false
            coroutineScope.launch {
                delay(throttleTime)
                onClick()
                isClickable = true
            }
        }
    }
}

inline fun Modifier.throttledNoRippleClickable(
    throttleTime: Long = 2000L,
    crossinline onClick: () -> Unit,
) = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    Modifier.noRippleClickable {
        val now = System.currentTimeMillis()
        if (now - lastClickTime > throttleTime) {
            lastClickTime = now
            onClick()
        }
    }
}

/**
 * Bring content into view
 *
 * 텍스트 필드에 포커스될 시, 화면 안으로 컴포넌트를 가져오는 확장 함수
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.bringContentIntoView(
    delay: Long = 300L,
): Modifier = composed {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    this
        .bringIntoViewRequester(bringIntoViewRequester)
        .onFocusChanged { focusState ->
            if (focusState.hasFocus) {
                coroutineScope.launch {
                    delay(delay)
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
}
