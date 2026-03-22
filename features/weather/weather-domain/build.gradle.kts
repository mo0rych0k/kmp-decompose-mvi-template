plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.weather.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
            api(projects.common.coreDomain)
        }
    }
}
