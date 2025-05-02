import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
}

android {
    setNameSpace("data.store")
}

dependencies {
    implementation(projects.data.remote)
    implementation(libs.timber)
    implementation(project(":domain:store"))
}
