package io.pylyp.sample.composeapp.roating.mapper

import io.pylyp.core.navigation.AppFeature
import io.pylyp.sample.composeapp.roating.DefaultAppRootComponent.AppRootConfig

internal fun AppFeature.toConfig(): AppRootConfig {
    return when (this) {
        AppFeature.Coffee -> AppRootConfig.Coffee
        AppFeature.Cover -> AppRootConfig.Cover
    }
}