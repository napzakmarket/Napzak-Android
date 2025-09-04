import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("feature.login")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.mixpanel)
    implementation(projects.domain.store)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.home)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.kakao.v2.user)
    implementation(libs.mixpanel)
}