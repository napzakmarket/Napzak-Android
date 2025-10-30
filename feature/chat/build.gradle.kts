import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("feature.chat")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.mixpanel)

    implementation(projects.domain.product)
    implementation(projects.domain.chat)
    implementation(projects.domain.presignedUrl)
    implementation(projects.domain.store)
    implementation(projects.domain.notification)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(libs.mixpanel)
}