package io.pylyp.weather.domain.usecase

import io.pylyp.common.core.foundation.entity.SuspendUseCase
import io.pylyp.weather.domain.entity.CurrentWeatherDD
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.domain.repository.WeatherRepository

public class GetKyivWeatherUseCase(
    private val repository: WeatherRepository,
) : SuspendUseCase<WeatherServiceType, CurrentWeatherDD>() {
    override suspend fun execute(parameters: WeatherServiceType): CurrentWeatherDD {
        return repository.getCurrentWeatherKyiv(service = parameters)
    }
}
