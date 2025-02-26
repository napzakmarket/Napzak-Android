plugins {
    id("com.napzak.market.buildlogic.convention.application")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    namespace = "com.napzak.market"
}

dependencies {
    implementation(projects.feature.main)
}