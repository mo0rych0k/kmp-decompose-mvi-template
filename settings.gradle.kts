rootProject.name = "kmp-decompose-mvi-template"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
    }
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":androidApp")
include(":composeApp")
include(":server")

include(
    ":common:core-di",
    ":common:core-navigation",
    ":common:core-network",
    ":common:core-threading",
    ":common:core-threading-test",
    ":common:testkit",
    ":common:uikit",
)

include(":features:cover:cover-ui")

include(
    ":features:coffee:coffee-data",
    ":features:coffee:coffee-data-network",
    ":features:coffee:coffee-domain",
    ":features:coffee:coffee-ui",
)
include(":common:persistence:persistence-database")
include(":common:core-domain")
include(":common:utils:logging")
include(":common:utils:logging-impl")
include(":common:app-info")
include(":common:core-ui")
