package com.napzak.market.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.navigation.keys.ScreenKey

interface EntryProviderBuilder {
    fun EntryProviderScope<ScreenKey>.provide()
}
