import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.ksp)
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
            /*common*/
            implementation(projects.common.appInfo)
            implementation(projects.common.uikit)
            implementation(projects.common.coreNavigation)
            implementation(projects.common.coreDi)
            implementation(projects.common.coreNetwork)
            implementation(projects.common.coreThreading)
            implementation(projects.common.persistence.persistenceDatabase)

            /*feature-cover*/
            implementation(projects.features.cover.coverUi)

            /*feature-coffee*/
            implementation(projects.features.coffee.coffeeData)
            implementation(projects.features.coffee.coffeeDataNetwork)
            implementation(projects.features.coffee.coffeeDomain)
            implementation(projects.features.coffee.coffeeUi)


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)

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

tasks.register("jvmRun") {
    group = "application"
    description = "Alias to run the Compose Desktop app (delegates to 'run')"
    dependsOn("run")
}
