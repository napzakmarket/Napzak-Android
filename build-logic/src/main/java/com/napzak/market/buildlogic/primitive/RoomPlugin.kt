package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.ksp
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import com.napzak.market.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.library("androidx-room-runtime"))
                implementation(libs.library("androidx-room-ktx"))
                ksp(libs.library("androidx-room-compiler"))
                testImplementation(libs.library("androidx-room-testing"))
            }
        }
    }
}