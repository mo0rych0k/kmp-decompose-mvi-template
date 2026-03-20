package io.pylyp.weather.domain.di

import io.pylyp.weather.domain.usecase.GetKyivWeatherUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val weatherDomainModule: Module = module {
    singleOf(::GetKyivWeatherUseCase)
}
