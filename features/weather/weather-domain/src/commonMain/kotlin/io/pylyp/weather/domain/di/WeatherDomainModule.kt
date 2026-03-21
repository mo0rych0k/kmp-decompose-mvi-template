package io.pylyp.weather.domain.di

import io.pylyp.weather.domain.usecase.DeleteWeatherObservationUseCase
import io.pylyp.weather.domain.usecase.GetWeatherObservationByIdUseCase
import io.pylyp.weather.domain.usecase.LoadSkyTrackBackgroundWeatherUseCase
import io.pylyp.weather.domain.usecase.ObserveWeatherObservationLogsUseCase
import io.pylyp.weather.domain.usecase.SaveWeatherObservationUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val weatherDomainModule: Module = module {
    singleOf(::ObserveWeatherObservationLogsUseCase)
    singleOf(::SaveWeatherObservationUseCase)
    singleOf(::DeleteWeatherObservationUseCase)
    singleOf(::GetWeatherObservationByIdUseCase)
    singleOf(::LoadSkyTrackBackgroundWeatherUseCase)
}
