import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.room")
}

android {
    setNameSpace("data.local")
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(projects.domain.notification)

    implementation(libs.androidx.room.paging)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
}