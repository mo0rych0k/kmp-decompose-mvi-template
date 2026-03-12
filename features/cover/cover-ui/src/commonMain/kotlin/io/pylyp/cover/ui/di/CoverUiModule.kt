package io.pylyp.cover.ui.di

import com.arkivanov.decompose.ComponentContext
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.AppFeature
import io.pylyp.cover.ui.roating.CoverRootComponent
import io.pylyp.cover.ui.roating.DefaultCoverRootComponent
import io.pylyp.cover.ui.screens.cover.CoverComponent
import io.pylyp.cover.ui.screens.cover.DefaultCoverComponent
import io.pylyp.cover.ui.screens.cover.store.CoverStore
import io.pylyp.cover.ui.screens.cover.store.CoverStoreFactory
import org.koin.core.component.get

internal fun ComponentFactory.createCoverStore(): CoverStore {
    return CoverStoreFactory(
        factory = get(),
    ).create()
}

internal fun ComponentFactory.createCoverComponent(
    componentContext: ComponentContext,
    onCloseFeature: () -> Unit,
    onNavigateToFeature: (appFeature: AppFeature) -> Unit,
): CoverComponent {
    return DefaultCoverComponent(
        componentContext = componentContext,
        componentFactory = get(),
        onCloseFeature = onCloseFeature,
        onNavigateToFeature = onNavigateToFeature
    )
}

public fun ComponentFactory.createCoverRootComponent(
    componentContext: ComponentContext,
    onCloseFeature: () -> Unit,
    onNavigateToFeature: (appFeature: AppFeature) -> Unit,
): CoverRootComponent {
    return DefaultCoverRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
        onCloseFeature = onCloseFeature,
        onNavigateToFeature = onNavigateToFeature
    )
}