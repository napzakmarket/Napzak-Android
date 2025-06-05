import java.util.Properties

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.napzak.market.buildlogic.primitive.retrofit")
    id("com.napzak.market.buildlogic.primitive.okhttp")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
android {
    namespace = "data.remote"

    buildTypes {
        debug {
            buildConfigField("String", "DUMMY_URL", properties.getProperty("dummy.url"))
            buildConfigField("String", "BASE_URL", properties.getProperty("base.url"))
            buildConfigField("String", "ACCESS_TOKEN", properties.getProperty("test.access.token"))
        }
    }
}

dependencies {
    implementation(projects.data.local)
    implementation(projects.core.util)
    implementation(libs.timber)
}
