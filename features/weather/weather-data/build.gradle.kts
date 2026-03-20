plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.weather.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.coreDi)
            implementation(projects.features.weather.weatherDataNetwork)
            implementation(projects.features.weather.weatherDomain)
        }
    }
}

