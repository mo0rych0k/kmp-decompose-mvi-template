package io.pylyp.weather.data.network.di

import io.pylyp.weather.data.network.MetNorwayRemoteDataSource
import io.pylyp.weather.data.network.MetNorwayRemoteDataSourceImpl
import io.pylyp.weather.data.network.OpenMeteoRemoteDataSource
import io.pylyp.weather.data.network.OpenMeteoRemoteDataSourceImpl
import io.pylyp.weather.data.network.WttrInRemoteDataSource
import io.pylyp.weather.data.network.WttrInRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val weatherDataNetworkModule: Module = module {
    factoryOf(::OpenMeteoRemoteDataSourceImpl) bind OpenMeteoRemoteDataSource::class
    factoryOf(::WttrInRemoteDataSourceImpl) bind WttrInRemoteDataSource::class
    factoryOf(::MetNorwayRemoteDataSourceImpl) bind MetNorwayRemoteDataSource::class
}
