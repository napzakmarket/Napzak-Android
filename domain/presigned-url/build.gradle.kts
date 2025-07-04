plugins {
    id("com.napzak.market.buildlogic.convention.kotlin")
}

dependencies {
    implementation(projects.core.util)
    implementation(libs.javax.inject)
    implementation(libs.coroutines.core)
}
