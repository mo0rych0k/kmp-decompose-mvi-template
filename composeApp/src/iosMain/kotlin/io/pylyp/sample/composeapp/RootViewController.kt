package io.pylyp.sample.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import io.pylyp.sample.composeapp.roating.AppRootComponent
import platform.UIKit.UIViewController

public fun rootViewController(root: AppRootComponent): UIViewController =
    ComposeUIViewController {
        App(root)
    }