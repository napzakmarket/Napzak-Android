package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.ksp
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import com.napzak.market.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                implementation(libs.library("hilt-android"))
                implementation(libs.library("hilt-core"))
                implementation(libs.library("hilt-navigation-compose"))
                testImplementation(libs.library("hilt-android-testing"))
                ksp(libs.library("hilt-compiler"))
                ksp(libs.library("hilt-android-compiler"))
                ksp(libs.library("androidx-hilt-compiler"))
            }
        }
    }

}