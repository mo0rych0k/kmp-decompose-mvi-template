package io.pylyp.cover.ui.di

import com.arkivanov.decompose.ComponentContext
import io.pylyp.cover.ui.roating.CoverRootComponent
import io.pylyp.cover.ui.roating.DefaultCoverRootComponent
import io.pylyp.cover.ui.screens.cover.CoverComponent
import io.pylyp.cover.ui.screens.cover.DefaultCoverComponent
import io.pylyp.cover.ui.screens.cover.store.CoverStore
import io.pylyp.cover.ui.screens.cover.store.CoverStoreFactory
import io.pylyp.network.core.di.ComponentFactory
import org.koin.core.component.get

internal fun ComponentFactory.createCoverStore(): CoverStore {
    return CoverStoreFactory(
        factory = get(),
    ).create()
}

internal fun ComponentFactory.createCoverComponent(
    componentContext: ComponentContext,
): CoverComponent {
    return DefaultCoverComponent(
        componentContext = componentContext,
        componentFactory = get(),
    )
}

public fun ComponentFactory.createCoverRootComponent(
    componentContext: ComponentContext,
): CoverRootComponent {
    return DefaultCoverRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
    )
}