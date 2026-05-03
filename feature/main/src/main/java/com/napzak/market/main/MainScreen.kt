package com.napzak.market.main

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.napzak.market.designsystem.component.scaffold.LocalInnerPadding
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.NapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.main.R.string.main_snack_bar_finish
import com.napzak.market.main.component.MainBottomBar
import com.napzak.market.main.component.MainRegisterDialog
import com.napzak.market.main.navigation.MainNavigator
import com.napzak.market.main.navigation.MainTab
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.PurchaseRegistrationScreenKey
import com.napzak.market.navigation.keys.SaleRegistrationScreenKey
import com.napzak.market.navigation.util.napzakDefaultTransitionSpec
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator,
    entryBuilders: Set<EntryProviderBuilder>,
    napzakToast: NapzakToast,
) {
    LaunchedEffect(Unit) {
        navigator.appNavigator.handleDeepLinkEvent()
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigateTab,
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalNapzakToast provides napzakToast,
            LocalInnerPadding provides innerPadding,
        ) {
            Box {
                MainRegisterDialog(
                    visibility = navigator.isRegister,
                    onSellRegisterClick = {
                        with(navigator) {
                            setRegistrationTabSelected(false)
                            this.appNavigator.navigateTo(SaleRegistrationScreenKey())
                        }
                    },
                    onBuyRegisterClick = {
                        with(navigator) {
                            setRegistrationTabSelected(false)
                            this.appNavigator.navigateTo(PurchaseRegistrationScreenKey())
                        }
                    },
                    onDismissRequest = navigator::navigateUp,
                    modifier = Modifier
                        .padding(innerPadding)
                        .zIndex(1f),
                )

                MainNavDisplay(
                    navigator = navigator,
                    entryBuilders = entryBuilders,
                )
            }
        }
    }
}

@Composable
private fun MainNavDisplay(
    navigator: MainNavigator,
    entryBuilders: Set<EntryProviderBuilder>,
) {
    val context = LocalContext.current
    val toast = LocalNapzakToast.current
    var backPressedTime by remember { mutableLongStateOf(0) }

    NavDisplay(
        backStack = navigator.appNavigator.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = { napzakDefaultTransitionSpec() },
        popTransitionSpec = { napzakDefaultTransitionSpec() },
        predictivePopTransitionSpec = { napzakDefaultTransitionSpec() },
        onBack = {
            when {
                navigator.isRegister -> {
                    navigator.setRegistrationTabSelected(false)
                }

                navigator.currentScreen is HomeScreenKey -> {
                    if (System.currentTimeMillis() - backPressedTime <= 3000) {
                        (context as Activity).finish()
                    } else {
                        toast.makeText(
                            message = context.getString(main_snack_bar_finish),
                            yOffset = toast.toastOffsetWithBottomBar(),
                            toastType = ToastType.COMMON,
                        )
                    }
                    backPressedTime = System.currentTimeMillis()
                }

                else -> {
                    navigator.navigateUp()
                }
            }
        },
        entryProvider = entryProvider {
            entryBuilders.forEach { builder -> with(builder) { provide() } }
        },
    )
}
