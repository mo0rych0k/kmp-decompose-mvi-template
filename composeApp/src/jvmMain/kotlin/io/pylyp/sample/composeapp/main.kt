package io.pylyp.sample.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.pylyp.sample.composeapp.di.initKoinInPlatform

public fun main(): Unit = application {
    initKoinInPlatform()
    Window(
        onCloseRequest = ::exitApplication,
        title = "SampleKMPMVI",
    ) {
        App()
    }
}