package com.napzak.market

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NapzakApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}