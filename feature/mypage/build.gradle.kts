import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("feature.mypage")

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")
    }
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.domain.store)
    implementation(projects.domain.notification)
    implementation(projects.feature.detail)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.timber)
}