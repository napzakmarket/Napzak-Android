package com.napzak.market

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.common.KakaoSdk
import com.napzak.market.notification.repository.FirebaseRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

private const val COMPRESSED = "compressed"

@HiltAndroidApp
class NapzakApplication : Application() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    private val lifecycleOwner: LifecycleOwner
        get() = ProcessLifecycleOwner.get()

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        initTimber()
        setDayMode()
        getPushToken()
        clearCacheDir()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun getPushToken() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                firebaseRepository.getPushTokenFromFirebase()
            }
        }
    }

    private fun clearCacheDir() {
        val dir = File(cacheDir, COMPRESSED)

        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                if (dir.exists()) dir.deleteRecursively()
            }.onFailure {
                Timber.e(it, "Failed to clear compressed cache")
            }
        }
    }
}
