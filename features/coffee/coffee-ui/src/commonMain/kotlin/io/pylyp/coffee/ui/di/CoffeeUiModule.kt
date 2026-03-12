package io.pylyp.coffee.ui.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.utils.internal.InternalMviKotlinApi
import io.pylyp.coffee.ui.roating.CoffeeRootComponent
import io.pylyp.coffee.ui.roating.DefaultCoffeeRootComponent
import io.pylyp.coffee.ui.screens.details.DefaultDetailsComponent
import io.pylyp.coffee.ui.screens.details.DefaultDetailsComponent.Output
import io.pylyp.coffee.ui.screens.details.DetailsComponent
import io.pylyp.coffee.ui.screens.details.store.DetailsStore
import io.pylyp.coffee.ui.screens.details.store.DetailsStoreFactory
import io.pylyp.coffee.ui.screens.gallery.DefaultGalleryComponent
import io.pylyp.coffee.ui.screens.gallery.GalleryComponent
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStore
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStoreFactory
import io.pylyp.common.core.di.ComponentFactory
import org.koin.core.component.get

internal fun ComponentFactory.createGalleryStore(): GalleryStore {
    return GalleryStoreFactory(
        factory = get(),
        coffeeListFlowUseCase = get(),
        fetchNewPageUseCase = get()
    ).create()
}

@OptIn(InternalMviKotlinApi::class)
internal fun ComponentFactory.createGalleryComponent(
    componentContext: ComponentContext,
    output: (DefaultGalleryComponent.Output) -> Unit,
): GalleryComponent {
    return DefaultGalleryComponent(
        componentContext = componentContext,
        componentFactory = get(),
        output = output,
    )
}

internal fun ComponentFactory.createDetailsStore(showedImageIndex: Int): DetailsStore {
    return DetailsStoreFactory(
        factory = get(),
        coffeeListFlowUseCase = get(),
        showedImageIndex = showedImageIndex
    ).create()
}

internal fun ComponentFactory.createDetailsComponent(
    componentContext: ComponentContext,
    output: (output: Output) -> Unit,
    showedImageIndex: Int,
): DetailsComponent {
    return DefaultDetailsComponent(
        componentContext = componentContext,
        componentFactory = get(),
        output = output,
        showedImageIndex = showedImageIndex
    )
}

public fun ComponentFactory.createCoffeeRootComponent(
    componentContext: ComponentContext,
): CoffeeRootComponent {
    return DefaultCoffeeRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
    )
}