import java.util.Properties

plugins {
    id("com.napzak.market.buildlogic.convention.application")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.android.application")
    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    load(File(rootDir, "local.properties").inputStream())
}

val kakaoAppKey = localProperties.getProperty("kakao.app.key")

android {
    namespace = "com.napzak.market"

    val kakaoKey = "kakao$kakaoAppKey"

    defaultConfig {
        resValue("string", "kakao_key", kakaoKey)
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        }
        getByName("release") {
            buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        }
    }
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

dependencies {
    implementation(projects.feature.main)

    implementation(projects.data.remote)
    implementation(projects.data.local)
    implementation(projects.data.banner)
    implementation(projects.data.store)
    implementation(projects.data.genre)
    implementation(projects.data.interest)
    implementation(projects.data.presignedUrl)
    implementation(projects.data.product)
    implementation(projects.data.registration)
    implementation(projects.data.report)
    implementation(projects.data.store)

    implementation(libs.androidx.appcompat)
    implementation(libs.timber)
    implementation(libs.v2.user)
    implementation(libs.v2.common)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.multidex)
    implementation(libs.lottie.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
}