import com.napzak.market.buildlogic.dsl.setNameSpace

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
}

android {
    setNameSpace("navigation")
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
    api(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(projects.core.common)
}
