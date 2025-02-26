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
        register("com.napzak.market.buildlogic.primitive.CommonAndroidPlugin") {
            id = "com.napzak.market.buildlogic.primitive.common"
            implementationClass = "com.napzak.market.buildlogic.primitive.CommonAndroidPlugin"
        }

        register("com.napzak.market.buildlogic.primitive.ComposePlugin") {
            id = "com.napzak.market.buildlogic.primitive.compose"
            implementationClass = "com.napzak.market.buildlogic.primitive.ComposePlugin"
        }

        register("com.napzak.market.buildlogic.primitive.HiltPlugin") {
            id = "com.napzak.market.buildlogic.primitive.hilt"
            implementationClass = "com.napzak.market.buildlogic.primitive.HiltPlugin"
        }

        register("com.napzak.market.buildlogic.primitive.KotlinPlugin") {
            id = "com.napzak.market.buildlogic.primitive.kotlin"
            implementationClass = "com.napzak.market.buildlogic.primitive.KotlinPlugin"
        }

        register("com.napzak.market.buildlogic.primitive.RetrofitPlugin") {
            id = "com.napzak.market.buildlogic.primitive.retrofit"
            implementationClass = "com.napzak.market.buildlogic.primitive.RetrofitPlugin"
        }

        register("com.napzak.market.buildlogic.primitive.OkHttpPlugin") {
            id = "com.napzak.market.buildlogic.primitive.okhttp"
            implementationClass = "com.napzak.market.buildlogic.primitive.OkHttpPlugin"
        }

        register("com.napzak.market.buildlogic.primitive.TestPlugin") {
            id = "com.napzak.market.buildlogic.primitive.test"
            implementationClass = "com.napzak.market.buildlogic.primitive.TestPlugin"
        }

        register("com.napzak.market.buildlogic.convention.ApplicationPlugin") {
            id = "com.napzak.market.buildlogic.convention.application"
            implementationClass = "com.napzak.market.buildlogic.convention.ApplicationPlugin"
        }

        register("com.napzak.market.buildlogic.convention.ComposePlugin") {
            id = "com.napzak.market.buildlogic.convention.compose"
            implementationClass = "com.napzak.market.buildlogic.convention.ComposePlugin"
        }

        register("com.napzak.market.buildlogic.convention.FeaturePlugin") {
            id = "com.napzak.market.buildlogic.convention.feature"
            implementationClass = "com.napzak.market.buildlogic.convention.FeaturePlugin"
        }
    }
}