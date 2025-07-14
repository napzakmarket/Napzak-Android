import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
}

android {
    setNameSpace("data.chat")
}

dependencies {
    implementation(projects.data.remote)
    implementation(projects.domain.chat)
    implementation(projects.core.util)

    implementation(libs.timber)
    implementation(libs.retrofit.gson.converter)

    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}