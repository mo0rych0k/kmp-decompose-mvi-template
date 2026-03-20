plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
}

val modulePackage = "io.pylyp.common.resources"

kotlin {
    androidLibrary {
        namespace = modulePackage
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        androidResources.enable = true
    }

    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.components.resources)
            implementation(libs.compose.runtime)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.pylyp.common.resources"
    generateResClass = always
}
