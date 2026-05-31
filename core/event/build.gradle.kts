import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("event")
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.coroutines.core)
}
