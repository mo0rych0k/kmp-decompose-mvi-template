import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.app.composeMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

val appPackageName = "io.pylyp.sample.composeapp"

kotlin {
    androidLibrary {
        namespace = appPackageName
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", appPackageName)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.work.runtime.ktx)
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
            implementation(projects.common.shareKit)
            implementation(projects.common.resources)
            implementation(libs.kotlinx.datetime)

            /*feature-cover*/
            implementation(projects.features.cover.coverUi)

            /*feature-coffee*/
            implementation(projects.features.coffee.coffeeData)
            implementation(projects.features.coffee.coffeeDataNetwork)
            implementation(projects.features.coffee.coffeeDomain)
            implementation(projects.features.coffee.coffeeUi)

            /*feature-weather*/
            implementation(projects.features.weather.weatherData)
            implementation(projects.features.weather.weatherDataNetwork)
            implementation(projects.features.weather.weatherDomain)
            implementation(projects.features.weather.weatherUi)


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
        mainClass = "${appPackageName}.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appPackageName
            packageVersion = "1.0.0"
            macOS {
                iconFile.set(project.file("../media/appicon/MacOsIcon.icns"))
            }
            windows {
                iconFile.set(project.file("../media/appicon/WindowsIcon.ico"))
            }
            linux {
                iconFile.set(project.file("../media/appicon/LinuxIcon.png"))
            }
        }
    }
}

tasks.register("jvmRun") {
    group = "application"
    description = "Alias to run the Compose Desktop app (delegates to 'run')"
    dependsOn("run")
}
