plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.weather.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
            implementation(projects.common.coreUi)
            implementation(projects.common.resources)
            implementation(projects.features.weather.weatherDomain)
        }
    }
}
