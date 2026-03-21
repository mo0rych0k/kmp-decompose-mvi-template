package io.pylyp.sample.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.common.core.di.IsolatedKoinContext.koin
import io.pylyp.sample.composeapp.di.bootstrapPlatformKoin
import io.pylyp.sample.composeapp.di.createAppRootComponent
import javax.swing.SwingUtilities

public fun main() {
    bootstrapPlatformKoin()

    val lifecycle = LifecycleRegistry()
    val componentFactory: ComponentFactory by koin().inject()

    val root = runOnUiThread {
        componentFactory.createAppRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )
    }

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "SampleKMPMVI",
        ) {
            App(root)
        }
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}
