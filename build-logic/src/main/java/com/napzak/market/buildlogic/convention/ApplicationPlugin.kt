package com.napzak.market.buildlogic.convention

import com.napzak.market.buildlogic.dsl.androidApplicationExtension
import com.napzak.market.buildlogic.dsl.configureAndroidLibrary
import com.napzak.market.buildlogic.dsl.libs
import com.napzak.market.buildlogic.dsl.version
import com.napzak.market.buildlogic.primitive.CommonAndroidPlugin
import com.napzak.market.buildlogic.primitive.KotlinPlugin
import com.napzak.market.buildlogic.primitive.TestPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            apply<KotlinPlugin>()
            apply<CommonAndroidPlugin>()
            apply<ComposePlugin>()
            apply<TestPlugin>()
            configureAndroidLibrary()

            androidApplicationExtension {
                composeOptions {
                    kotlinCompilerExtensionVersion =
                        libs.findVersion("kotlinCompilerExtensionVersion").get().requiredVersion
                }

                defaultConfig {
                    applicationId = "com.napzak.market"
                    versionCode = libs.version("versionCode").toInt()
                    versionName = libs.version("versionName")
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
    }
}