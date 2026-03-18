plugins {
    `kotlin-dsl`
}

group = "io.pylyp.build.logic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "io.pylyp.kotlin.multiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "io.pylyp.kotlin.multiplatformLibrary"
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "io.pylyp.kotlin.composeMultiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("iosVersionUpdate") {
            id = "io.pylyp.ios.versionUpdate"
            implementationClass = "io.pylyp.build.logic.IosVersionUpdatePlugin"
        }
    }
}
