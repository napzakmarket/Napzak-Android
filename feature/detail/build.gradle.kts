import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("feature.detail")
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)

    implementation(projects.domain.product)
    implementation(projects.domain.genre)
    implementation(projects.domain.interest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.timber)
}