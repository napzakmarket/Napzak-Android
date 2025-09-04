import java.util.Properties

plugins {
    id("com.napzak.market.buildlogic.convention.application")
    id("com.napzak.market.buildlogic.primitive.hilt")
    id("com.google.gms.google-services")
}

val localProps = Properties().apply {
    val f = File(rootDir, "local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

val keystoreProperties = Properties().apply {
    val f = File(rootDir, "app/signing/keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

val kakaoNativeKey: String =
    providers.gradleProperty("KAKAO_APP_KEY").orNull
        ?: System.getenv("KAKAO_APP_KEY")
        ?: localProps.getProperty("kakao.app.key")
        ?: throw GradleException("KAKAO_APP_KEY (or local kakao.app.key) is missing")

android {
    namespace = "com.napzak.market"

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keystoreProperties.getProperty("keystorePath"))
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    defaultConfig {
        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoNativeKey\"")
        manifestPlaceholders["kakaoScheme"] = "kakao$kakaoNativeKey"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            manifestPlaceholders["appName"] = "@string/app_name_dev"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_app_dev"
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appName"] = "@string/app_name"
            manifestPlaceholders["appIcon"] = "@drawable/ic_app"
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
    implementation(projects.data.chat)
    implementation(projects.data.notification)

    implementation(projects.domain.notification)

    implementation(libs.androidx.appcompat)
    implementation(libs.timber)
    implementation(libs.kakao.v2.user)
    implementation(libs.kakao.v2.common)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.multidex)
    implementation(libs.lottie.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.lifecycle.ktx)
    implementation(libs.androidx.browser)
}