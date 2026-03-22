package io.pylyp.weather.data.di

import io.pylyp.weather.data.WeatherObservationRepositoryImpl
import io.pylyp.weather.data.WeatherRepositoryImpl
import io.pylyp.weather.data.location.createDeviceLocationProvider
import io.pylyp.weather.data.location.createPlaceLabelProvider
import io.pylyp.weather.domain.location.DeviceLocationProvider
import io.pylyp.weather.domain.location.PlaceLabelProvider
import io.pylyp.weather.domain.repository.WeatherObservationRepository
import io.pylyp.weather.domain.repository.WeatherRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val weatherDataModule: Module = module {
    factoryOf(::WeatherRepositoryImpl) bind WeatherRepository::class
    factoryOf(::WeatherObservationRepositoryImpl) bind WeatherObservationRepository::class
    single<DeviceLocationProvider> { createDeviceLocationProvider() }
    single<PlaceLabelProvider> { createPlaceLabelProvider() }
}
