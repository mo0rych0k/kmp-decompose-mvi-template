plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.core.navigation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization)
            api(libs.decompose)
            api(libs.decompose.compose)
            api(libs.essenty.lifecycle.coroutines)
            api(libs.mvikotlin)
            api(libs.mvikotlin.main)
            api(libs.mvikotlin.extensions.coroutines)
            api(libs.kotlinx.coroutinesCore)

            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
        }
    }
}
