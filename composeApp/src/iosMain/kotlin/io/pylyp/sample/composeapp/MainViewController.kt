package io.pylyp.sample.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.common.core.di.IsolatedKoinContext
import io.pylyp.sample.composeapp.di.createAppRootComponent
import io.pylyp.sample.composeapp.roating.AppRootComponent
import platform.UIKit.UIViewController

/**
 * iOS shell entry — same wiring as Android [com.pylyp.sample.MainActivity].
 * Swift: `MainViewControllerKt.MainViewController()` after `KoinHelperKt.bootstrapPlatformKoin()`.
 */
@Suppress("unused")
public fun MainViewController(): UIViewController = ComposeUIViewController {
    App(IosAppRootHolder.appRoot)
}

private object IosAppRootHolder {
    val appRoot: AppRootComponent by lazy {
        val factory: ComponentFactory = IsolatedKoinContext.koin().get()
        factory.createAppRootComponent(
            componentContext = DefaultComponentContext(lifecycle = ApplicationLifecycle()),
        )
    }
}
