package io.pylyp.weather.domain.usecase

import io.pylyp.common.core.foundation.entity.SuspendUseCase
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.repository.WeatherObservationRepository

public class GetWeatherObservationByIdUseCase(
    private val repository: WeatherObservationRepository,
) : SuspendUseCase<Long, WeatherObservationRecordDD?>() {
    override suspend fun execute(parameters: Long): WeatherObservationRecordDD? {
        return repository.getById(parameters)
    }
}
