import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.sample.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.withType<Framework>().configureEach {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "io.pylyp.sample.composeapp")
        }
    }

    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(projects.common.uikit)
            implementation(projects.common.coreNavigation)
            implementation(projects.common.coreDi)
            implementation(projects.features.cover.coverUi)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.pylyp.sample.composeapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.pylyp.sample.composeapp"
            packageVersion = "1.0.0"
        }
    }
}
