import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.convention.compose")
    id("com.napzak.market.buildlogic.primitive.okhttp")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("util")
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}