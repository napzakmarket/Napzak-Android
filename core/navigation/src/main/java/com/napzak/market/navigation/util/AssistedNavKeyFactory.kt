package com.napzak.market.navigation.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.navigation.keys.ScreenKey

interface AssistedNavKeyFactory<V : ViewModel, K> {
    fun create(key: K): V
}

/**
 * [ViewModel]에 [ScreenKey]를 주입하기 위해 기능을 확장한 EntryProvider입니다.
 *
 * 이 확장함수가 동작하기 위해서 [ViewModel]은 다음과 같이 구현되어야 합니다.
 *
 * ```kotlin
 *
 * @HiltViewModel(assistedFactory = ExampleViewModel.Factory::class)
 * internal class ExampleViewModel @AssistedInject constructor(
 *     @Assisted val navKey: ExampleScreenKey,
 *     ...
 * ) : ViewModel {
 *
 *     @AssistedFactory
 *     interface Factory : AssistedNavKeyFactory<ExampleViewModel, ExampleScreenKey>
 *     ...
 * }
 * ```
 */
inline fun <reified VM : ViewModel, reified F : AssistedNavKeyFactory<VM, K>, reified K : ScreenKey>
        EntryProviderScope<ScreenKey>.assistedEntry(
    crossinline block: @Composable (key: K, viewModel: VM) -> Unit,
) {
    entry<K> { key ->
        val viewModel = hiltViewModel<VM, F>(creationCallback = { it.create(key) })
        block(key, viewModel)
    }
}
