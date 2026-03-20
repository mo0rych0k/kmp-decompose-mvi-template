plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.core.di"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.common.appInfo)
            api(libs.koin.core)
            api(libs.koin.compose)
        }

        commonTest.dependencies {
            implementation(projects.common.coreThreadingTest)
        }
    }
}
