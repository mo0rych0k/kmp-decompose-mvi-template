import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.app.composeMultiplatform)
}

project.extensions.configure<ApplicationExtension> {
    namespace = "com.pylyp.sample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pylyp.sample"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.app.build.get().toInt()
        versionName = libs.versions.app.version.get()
    }

    buildFeatures {
        aidl = false
        viewBinding = false
        prefab = false
        shaders = false
        buildConfig = true
        resValues = true
        compose = true
    }

//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.common.coreDi)
    implementation(projects.common.uikit)
    implementation(projects.common.coreNavigation)
    implementation(libs.koin.android)
    implementation(libs.androidx.activity.compose)
}
