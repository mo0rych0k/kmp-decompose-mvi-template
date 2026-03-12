plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.app.info"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }
}
