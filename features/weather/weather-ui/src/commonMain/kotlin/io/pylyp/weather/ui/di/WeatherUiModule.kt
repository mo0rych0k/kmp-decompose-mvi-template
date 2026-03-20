package io.pylyp.weather.ui.di

import com.arkivanov.decompose.ComponentContext
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.weather.domain.entity.WeatherServiceType
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
