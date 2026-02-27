plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotlinMultiplatformLibrary) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.gradleAndroidCacheFix) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.app.iosVersionUpdate)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    configurations.all {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
    }
    plugins.withType<com.android.build.gradle.api.AndroidBasePlugin>() {
        apply(plugin = "org.gradle.android.cache-fix")
    }
}