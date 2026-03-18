plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.coffee.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)

            implementation(projects.common.coreUi)
            implementation(projects.common.coreNavigation)
            implementation(projects.common.coreDi)

            implementation(projects.features.coffee.coffeeDomain)
        }
    }
}
