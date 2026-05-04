package com.napzak.market.buildlogic.dsl

import org.gradle.api.Project

fun Project.configureAndroidLibrary() {
    androidExtension().apply {
        compileSdk = libs.version("compileSdk").toInt()
        defaultConfig.apply {
            minSdk = libs.version("minSdk").toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}