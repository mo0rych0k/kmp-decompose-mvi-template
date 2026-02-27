package io.pylyp.sample.composeapp.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import io.pylyp.core.di.ComponentFactory
import io.pylyp.sample.composeapp.roating.AppRootComponent
import io.pylyp.sample.composeapp.roating.DefaultAppRootComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.dsl.module

public val appNavigationModule: Module = module {
    factory<StoreFactory> { DefaultStoreFactory() }
}

internal fun ComponentFactory.createAppRootComponent(
    componentContext: ComponentContext,
): AppRootComponent {
    return DefaultAppRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
    )
}