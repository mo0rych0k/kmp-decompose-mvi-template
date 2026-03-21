package io.pylyp.weather.data.network.di

import io.pylyp.weather.data.network.OpenWeatherRemoteDataSource
import io.pylyp.weather.data.network.OpenWeatherRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val weatherDataNetworkModule: Module = module {
    factoryOf(::OpenWeatherRemoteDataSourceImpl) bind OpenWeatherRemoteDataSource::class
}
