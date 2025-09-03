import com.napzak.market.buildlogic.dsl.implementation
import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
}

android {
    setNameSpace("data.update")
}

dependencies {

    implementation(projects.data.local)
    implementation(projects.domain.update)

    implementation(libs.timber)
}