package com.napzak.market.mixpanel

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.core.mixpanel.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MixpanelModule {
    @Provides
    @Singleton
    fun provideMixpanelApi(
        @ApplicationContext context: Context,
    ): MixpanelAPI? {
        return if (BuildConfig.DEBUG) {
            MixpanelAPI.getInstance(
                context,
                BuildConfig.MIXPANEL_TOKEN,
                false,
            ).apply {
                registerSuperProperties(
                    JSONObject(
                        mapOf(
                            "os" to "Android",
                        )
                    )
                )
            }
        } else null
    }
}
