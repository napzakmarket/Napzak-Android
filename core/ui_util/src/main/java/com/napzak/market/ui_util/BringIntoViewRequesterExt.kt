package com.napzak.market.ui_util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.ui.focus.FocusState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [FocusState]에 따라 [BringIntoViewRequester]가 붙은 컴포넌트를 화면 안으로 가져오는 메소드
 *
 * @param scope [BringIntoViewRequester.bringIntoView]가 실행되는 스코프
 * @param focusState 포커스 상태 객체
 * @param delayMillis [BringIntoViewRequester.bringIntoView] 호출을 지연시키는 시간 (밀리초 단위)
 */
@OptIn(ExperimentalFoundationApi::class)
fun BringIntoViewRequester.bringIntoView(
    scope: CoroutineScope,
    focusState: FocusState,
    delayMillis: Long = 300L,
) {
    if (focusState.isFocused) {
        scope.launch {
            delay(delayMillis)
            bringIntoView()
        }
    }
}