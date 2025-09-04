import com.napzak.market.buildlogic.dsl.setNameSpace
import java.util.Properties
import kotlin.apply

plugins {
    id("com.napzak.market.buildlogic.convention.feature")
    id("com.napzak.market.buildlogic.primitive.hilt")
}

val properties = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    setNameSpace("core.mixpanel")

    defaultConfig {
        buildConfigField("String", "MIXPANEL_TOKEN", properties.getProperty("mixpanel.token"))
    }
}

dependencies {
    implementation(libs.mixpanel)
}
