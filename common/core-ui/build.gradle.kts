plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.core.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.components.resources)
            api(libs.compose.runtime)
            api(projects.common.uikit)
            api(projects.common.resources)
            api(projects.common.coreDi)
            api(projects.common.coreFoundation)
            api(projects.common.coreNavigation)
            api(projects.common.coreThreading)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
