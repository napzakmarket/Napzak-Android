package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.androidTestImplementation
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import com.napzak.market.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                testImplementation(libs.library("junit"))
                testImplementation(libs.library("kotlin-test"))
                androidTestImplementation(libs.library("androidx-junit"))
                androidTestImplementation(libs.library("androidx-espresso-core"))
            }
        }
    }
}
