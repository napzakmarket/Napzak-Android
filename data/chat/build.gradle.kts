import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
    id("com.napzak.market.buildlogic.primitive.room")
}

android {
    setNameSpace("data.chat")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.data.remote)
    implementation(projects.data.local)
    implementation(projects.domain.chat)
    implementation(projects.core.util)
    implementation(projects.core.common)

    implementation(libs.timber)
    implementation(libs.androidx.room.paging)

    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.room.testing)
}