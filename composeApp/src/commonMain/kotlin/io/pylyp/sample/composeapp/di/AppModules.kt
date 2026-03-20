package io.pylyp.sample.composeapp.di

import io.pylyp.api.core.di.networkEngineModule
import io.pylyp.api.core.di.networkModule
import io.pylyp.coffee.data.di.coffeeDataModule
import io.pylyp.coffee.data.network.di.coffeeDataNetworkModule
import io.pylyp.coffee.domain.di.coffeeDomainModule
import io.pylyp.common.core.persistence.di.persistenceDatabaseModule
import io.pylyp.common.core.persistence.di.persistenceDatabasePlatformModule
import io.pylyp.core.threading.di.coreThreadingModule
import io.pylyp.weather.data.di.weatherDataModule
import io.pylyp.weather.data.network.di.weatherDataNetworkModule
import io.pylyp.weather.domain.di.weatherDomainModule
import org.koin.core.module.Module

internal val appModules: List<Module> = listOf(
    networkModule,
    networkEngineModule,
    appNavigationModule,
    coreThreadingModule,
    persistenceDatabaseModule,
    persistenceDatabasePlatformModule,

    coffeeDataModule,
    coffeeDataNetworkModule,
    coffeeDomainModule,
    weatherDataModule,
    weatherDataNetworkModule,
    weatherDomainModule,
    platformModule,
)

internal expect val platformModule: Module
