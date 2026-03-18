plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.common.core.foundation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }
}
