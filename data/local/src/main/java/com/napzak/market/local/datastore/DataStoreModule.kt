package com.napzak.market.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val TOKEN_PREFERENCE_NAME = "token_preference"
    private const val NOTIFICATION_PREFERENCE_NAME = "notification_preference"

    private val Context.provideDataStore by preferencesDataStore(TOKEN_PREFERENCE_NAME)
    private val Context.provideNotificationDataStore by preferencesDataStore(
        NOTIFICATION_PREFERENCE_NAME
    )

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ) = TokenDataStore(context.provideDataStore)

    @Provides
    @Singleton
    fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ) = NotificationDataStore(context.provideNotificationDataStore)
}
