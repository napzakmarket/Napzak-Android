package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.androidExtension
import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        androidExtension().apply {
            defaultConfig.apply {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildFeatures.apply {
                buildConfig = true
            }
        }
        dependencies {
            implementation(libs.library("androidx-core-ktx"))
        }
    }
}