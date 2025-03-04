package com.napzak.market.buildlogic.dsl

import org.gradle.api.Project

fun Project.configureAndroidLibrary() {
    android {
        setCompileSdkVersion(libs.version("compileSdk").toInt())

        defaultConfig {
            minSdk = libs.version("minSdk").toInt()
            targetSdk = libs.version("targetSdk").toInt()

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}