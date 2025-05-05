plugins {
    id("com.napzak.market.buildlogic.convention.application")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    namespace = "com.napzak.market"
}

dependencies {
    implementation(projects.feature.main)

    implementation(projects.data.remote)
    implementation(projects.data.local)
    implementation(projects.data.banner)
    implementation(projects.data.dummy)
    implementation(projects.data.store)
    implementation(projects.data.genre)
    implementation(projects.data.interest)
    implementation(projects.data.presignedUrl)
    implementation(projects.data.product)
    implementation(projects.data.registration)
    implementation(projects.data.report)
    implementation(projects.data.store)

    implementation(libs.androidx.appcompat)
    implementation(libs.timber)
}