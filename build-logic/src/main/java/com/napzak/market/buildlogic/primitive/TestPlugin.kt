package com.napzak.market.buildlogic.primitive

import com.napzak.market.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "testImplementation"(libs.findBundle("unit-test").get())
            }
        }
    }
}
