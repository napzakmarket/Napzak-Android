import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("main")
}

dependencies {
    implementation(projects.feature.dummy)
    implementation(projects.feature.home)
    implementation(projects.feature.explore)
    implementation(projects.feature.search)
    implementation(projects.feature.report)
    implementation(projects.feature.store)
    implementation(projects.feature.detail)
    implementation(projects.feature.registration)
    implementation(projects.feature.mypage)

    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
}