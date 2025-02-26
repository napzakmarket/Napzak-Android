package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.implementationPlatform
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class OkHttpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.library(("okhttp")))
                implementation(libs.library(("okhttp-logging")))
                implementationPlatform(libs.library("okhttp-bom"))
            }
        }
    }
}