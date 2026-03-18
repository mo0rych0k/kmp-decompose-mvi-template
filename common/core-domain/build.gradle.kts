plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.core.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            api(projects.common.coreFoundation)
        }
    }
}
