package com.napzak.market.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs

class RetrofitPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.library(("retrofit")))
                implementation(libs.library(("retrofit2-kotlinx-serialization-converter")))
            }
        }
    }
}