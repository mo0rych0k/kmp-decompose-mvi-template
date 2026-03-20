package io.pylyp.weather.ui.di

import com.arkivanov.decompose.ComponentContext
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.domain.location.PlaceLabelProvider
import io.pylyp.weather.ui.roating.DefaultWeatherRootComponent
import io.pylyp.weather.ui.roating.WeatherRootComponent
import io.pylyp.weather.ui.screens.details.DefaultWeatherDetailsComponent
import io.pylyp.weather.ui.screens.details.WeatherDetailsComponent
import io.pylyp.weather.ui.screens.details.store.WeatherDetailsStore
import io.pylyp.weather.ui.screens.details.store.WeatherDetailsStoreFactory
import io.pylyp.weather.ui.screens.services.DefaultWeatherServicesComponent
import io.pylyp.weather.ui.screens.services.WeatherServicesComponent
import io.pylyp.weather.ui.screens.services.store.WeatherServicesStore
import io.pylyp.weather.ui.screens.services.store.WeatherServicesStoreFactory
import io.pylyp.weather.ui.skytrack.DefaultSkyTrackRootComponent
import io.pylyp.weather.ui.skytrack.SkyTrackRootComponent
import io.pylyp.weather.ui.skytrack.add.AddWeatherObservationComponent
import io.pylyp.weather.ui.skytrack.add.DefaultAddWeatherObservationComponent
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStore
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStoreFactory
import io.pylyp.weather.ui.skytrack.details.DefaultSkyTrackDetailsComponent
import io.pylyp.weather.ui.skytrack.details.SkyTrackDetailsComponent
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStoreFactory
import io.pylyp.weather.ui.skytrack.history.DefaultSkyTrackHistoryComponent
import io.pylyp.weather.ui.skytrack.history.SkyTrackHistoryComponent
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStore
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStoreFactory
import org.koin.core.component.get

internal fun ComponentFactory.createWeatherServicesStore(): WeatherServicesStore {
    return WeatherServicesStoreFactory(
        factory = get(),
    ).create()
}

internal fun ComponentFactory.createWeatherServicesComponent(
    componentContext: ComponentContext,
    output: (DefaultWeatherServicesComponent.Output) -> Unit,
): WeatherServicesComponent {
    return DefaultWeatherServicesComponent(
        componentContext = componentContext,
        componentFactory = get(),
        output = output,
    )
}

internal fun ComponentFactory.createWeatherDetailsStore(
    serviceType: WeatherServiceType,
): WeatherDetailsStore {
    return WeatherDetailsStoreFactory(
        factory = get(),
        kyivWeatherUseCase = get(),
        serviceType = serviceType,
    ).create()
}

internal fun ComponentFactory.createWeatherDetailsComponent(
    componentContext: ComponentContext,
    serviceType: WeatherServiceType,
    output: (DefaultWeatherDetailsComponent.Output) -> Unit,
): WeatherDetailsComponent {
    return DefaultWeatherDetailsComponent(
        componentContext = componentContext,
        componentFactory = get(),
        serviceType = serviceType,
        output = output,
    )
}

public fun ComponentFactory.createWeatherRootComponent(
    componentContext: ComponentContext,
    onFinished: () -> Unit,
): WeatherRootComponent {
    return DefaultWeatherRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
        onFinished = onFinished,
    )
}

internal fun ComponentFactory.createSkyTrackHistoryStore(): SkyTrackHistoryStore {
    return SkyTrackHistoryStoreFactory(
        factory = get(),
        observeLogsUseCase = get(),
    ).create()
}

internal fun ComponentFactory.createSkyTrackHistoryComponent(
    componentContext: ComponentContext,
    output: (DefaultSkyTrackHistoryComponent.Output) -> Unit,
): SkyTrackHistoryComponent {
    return DefaultSkyTrackHistoryComponent(
        componentContext = componentContext,
        componentFactory = get(),
        output = output,
    )
}

internal fun ComponentFactory.createAddWeatherObservationStore(): AddWeatherObservationStore {
    return AddWeatherObservationStoreFactory(
        factory = get(),
        loadBackgroundWeatherUseCase = get(),
        saveWeatherObservationUseCase = get(),
        placeLabelProvider = get<PlaceLabelProvider>(),
    ).create()
}

internal fun ComponentFactory.createAddWeatherObservationComponent(
    componentContext: ComponentContext,
    output: (DefaultAddWeatherObservationComponent.Output) -> Unit,
): AddWeatherObservationComponent {
    return DefaultAddWeatherObservationComponent(
        componentContext = componentContext,
        componentFactory = get(),
        output = output,
    )
}

internal fun ComponentFactory.createSkyTrackDetailsStore(
    recordId: Long,
): SkyTrackDetailsStore {
    return SkyTrackDetailsStoreFactory(
        factory = get(),
        getByIdUseCase = get(),
        deleteUseCase = get(),
        recordId = recordId,
    ).create()
}

internal fun ComponentFactory.createSkyTrackDetailsComponent(
    componentContext: ComponentContext,
    recordId: Long,
    output: (DefaultSkyTrackDetailsComponent.Output) -> Unit,
): SkyTrackDetailsComponent {
    return DefaultSkyTrackDetailsComponent(
        componentContext = componentContext,
        componentFactory = get(),
        recordId = recordId,
        output = output,
    )
}

public fun ComponentFactory.createSkyTrackRootComponent(
    componentContext: ComponentContext,
    onFinished: () -> Unit,
): SkyTrackRootComponent {
    return DefaultSkyTrackRootComponent(
        componentContext = componentContext,
        componentFactory = get(),
        onFinished = onFinished,
    )
}
