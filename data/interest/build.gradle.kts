import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
}

android {
    setNameSpace("data.interest")
}

dependencies {
    implementation(projects.data.remote)
    implementation(projects.domain.interest)

    implementation(libs.timber)
}