package com.napzak.market.buildlogic.convention

import com.napzak.market.buildlogic.dsl.androidExtension
import com.napzak.market.buildlogic.primitive.ComposePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<ComposePlugin>()

            androidExtension.apply {
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}