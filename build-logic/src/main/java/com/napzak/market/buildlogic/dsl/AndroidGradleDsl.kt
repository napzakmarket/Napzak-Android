package com.napzak.market.buildlogic.dsl

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

fun Project.android(
    action: TestedExtension.() -> Unit,
) {
    extensions.configure(action)
}

fun Project.androidApplicationExtension(
    action: ApplicationExtension.() -> Unit,
) {
    extensions.configure(action)
}

internal val Project.applicationExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<ApplicationExtension>()

internal val Project.libraryExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<LibraryExtension>()

internal val Project.androidExtension: CommonExtension<*, *, *, *, *, *>
    get() = runCatching { libraryExtension }
        .recoverCatching { applicationExtension }
        .onFailure { println("Could not find Library or Application extension from this project") }
        .getOrThrow()

fun LibraryExtension.setNameSpace(nameSpace: String) {
    namespace = "com.napzak.market.${nameSpace}"
}
