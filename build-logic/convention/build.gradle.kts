plugins {
    `kotlin-dsl`
}

group = "io.pylyp.build.logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
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