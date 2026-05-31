package com.napzak.market.navigationimpl

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavBackStack
import com.napzak.market.event.ChatSessionManager
import com.napzak.market.event.DeepLinkEventBus
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SplashScreenKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppNavigatorModule {

    @Provides
    fun provideBackStack(): SnapshotStateList<ScreenKey> {
        return mutableStateListOf(SplashScreenKey)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideNavBackStack(
        base: SnapshotStateList<ScreenKey>,
    ): NavBackStack<ScreenKey> {
        return NavBackStack(base)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideAppNavigator(
        backStack: NavBackStack<ScreenKey>,
        chatSessionManager: ChatSessionManager,
        deepLinkEventBus: DeepLinkEventBus,
    ): AppNavigator {
        return AppNavigatorImpl(backStack, chatSessionManager, deepLinkEventBus)
    }
}
