import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("data.local")
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(projects.domain.notification)
}