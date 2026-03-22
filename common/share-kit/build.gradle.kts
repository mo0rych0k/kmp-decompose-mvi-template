plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.sharekit"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.coreDi)
        }
        androidMain.dependencies {
            implementation(libs.koin.core)
        }
        iosMain.dependencies {
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
