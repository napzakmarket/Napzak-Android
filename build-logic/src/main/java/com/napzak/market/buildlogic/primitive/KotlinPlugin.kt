package com.napzak.market.buildlogic.primitive

import com.android.build.api.dsl.CommonExtension
import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.library
import com.napzak.market.buildlogic.dsl.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }

            extensions.findByType<CommonExtension>()?.apply {
                compileOptions.sourceCompatibility = JavaVersion.VERSION_17
                compileOptions.targetCompatibility = JavaVersion.VERSION_17
            }

            tasks.withType<KotlinCompile> {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            dependencies {
                implementation(libs.library("kotlinx-immutable"))
                implementation(libs.library("kotlinx-serialization-json"))
            }
        }
    }
}
