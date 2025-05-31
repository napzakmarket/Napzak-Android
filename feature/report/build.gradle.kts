import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("feature.report")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)

    implementation(projects.domain.report)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.timber)
}