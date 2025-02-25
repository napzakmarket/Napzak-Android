plugins {
    `kotlin-dsl`
}

group = "com.napzak.market.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.compose.compiler.gradle)
}

gradlePlugin {
    plugins {

    }
}
