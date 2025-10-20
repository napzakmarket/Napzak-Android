plugins {
    id("com.napzak.market.buildlogic.convention.kotlin")
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.coroutines.core)
    implementation(libs.androidx.paging.common)
}