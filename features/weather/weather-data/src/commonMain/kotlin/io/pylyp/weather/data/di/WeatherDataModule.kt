package io.pylyp.weather.data.di

import io.pylyp.weather.data.WeatherRepositoryImpl
import io.pylyp.weather.domain.repository.WeatherRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val weatherDataModule: Module = module {
    factoryOf(::WeatherRepositoryImpl) bind WeatherRepository::class
}
