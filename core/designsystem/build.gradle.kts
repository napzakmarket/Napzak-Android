import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
}

android {
    setNameSpace("designsystem")
}

dependencies {
    // Miscellaneous libraries
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
}