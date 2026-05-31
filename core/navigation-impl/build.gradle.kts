import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

android {
    setNameSpace("navigationimpl")
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.core.event)
    implementation(projects.core.common)
}
