import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

plugins {
    alias(libs.plugins.app.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

val openWeatherApiKeyForBuild: String =
    (findProperty("openweather.api.key") as? String)?.trim().orEmpty()
        .ifEmpty { System.getenv("OPENWEATHER_API_KEY").orEmpty() }

val openWeatherGeneratedSourceRoot = layout.buildDirectory.dir("generated/openweather")

val generateOpenWeatherAndroidSecret = tasks.register("generateOpenWeatherAndroidSecret") {
    outputs.dir(openWeatherGeneratedSourceRoot)
    inputs.property("openWeatherApiKey", openWeatherApiKeyForBuild)

    doLast {
        val root = openWeatherGeneratedSourceRoot.get().asFile
        val outFile = root.resolve("io/pylyp/weather/data/network/OpenWeatherApiKeyGenerated.kt")
        outFile.parentFile.mkdirs()
        val escaped = buildString {
            for (c in openWeatherApiKeyForBuild) {
                when (c) {
                    '\\' -> append("\\\\")
                    '"' -> append("\\\"")
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    '$' -> append("\\$")
                    else -> append(c)
                }
            }
        }
        outFile.writeText(
            """
            package io.pylyp.weather.data.network

            internal object OpenWeatherApiKeyGenerated {
                internal const val VALUE: String = "$escaped"
            }
            """.trimIndent() + "\n",
        )
    }
}

kotlin {
    androidLibrary {
        namespace = "io.pylyp.weather.data.network"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    targets.withType<KotlinAndroidTarget>().configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                dependsOn(generateOpenWeatherAndroidSecret)
            }
        }
    }

    sourceSets {
        androidMain.get().kotlin.srcDir(openWeatherGeneratedSourceRoot)

        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
            implementation(projects.common.coreDi)
            implementation(projects.common.coreNetwork)
            implementation(projects.common.coreThreading)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(projects.common.testkit)
        }
    }
}
