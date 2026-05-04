package com.napzak.market.buildlogic.dsl

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

fun Project.androidApplicationExtension(
    action: ApplicationExtension.() -> Unit,
) {
    extensions.configure(action)
}

internal fun Project.androidExtension() = extensions.getByType(CommonExtension::class)

fun CommonExtension.setNameSpace(nameSpace: String) {
    namespace = "com.napzak.market.${nameSpace}"
}
