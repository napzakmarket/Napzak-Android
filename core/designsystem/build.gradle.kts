import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
}

android {
    setNameSpace("designsystem")
}

dependencies {
    implementation(projects.core.util)

    // Miscellaneous libraries
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
}