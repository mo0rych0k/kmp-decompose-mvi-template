package io.pylyp.sample.composeapp

import androidx.compose.runtime.Composable
import io.pylyp.common.uikit.AppTheme
import io.pylyp.sample.composeapp.roating.AppRootComponent
import io.pylyp.sample.composeapp.roating.AppRootMain

@Composable
public fun App(rootComponent: AppRootComponent) {
    AppTheme { AppRootMain(rootComponent = rootComponent) }
}