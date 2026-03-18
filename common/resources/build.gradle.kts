plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.resources"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.components.resources)
        }
    }
}

compose.resources {
    packageOfResClass = "io.pylyp.common.resources.internal"
    publicResClass = true
}
