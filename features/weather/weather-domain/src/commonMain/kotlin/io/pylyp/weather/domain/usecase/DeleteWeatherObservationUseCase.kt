package io.pylyp.weather.domain.usecase

import io.pylyp.common.core.foundation.entity.SuspendUseCase
import io.pylyp.weather.domain.repository.WeatherObservationRepository

public class DeleteWeatherObservationUseCase(
    private val repository: WeatherObservationRepository,
) : SuspendUseCase<Long, Unit>() {
    override suspend fun execute(parameters: Long) {
        repository.deleteById(parameters)
    }
}
