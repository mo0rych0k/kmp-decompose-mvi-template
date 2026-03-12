plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.utils.logging.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kermit)
            implementation(projects.common.coreDi)
            implementation(projects.common.utils.logging)
            implementation(projects.common.utils.loggingImpl)
        }
    }
}
