package io.pylyp.weather.domain.usecase

import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.repository.WeatherObservationRepository
import kotlinx.coroutines.flow.Flow

public class ObserveWeatherObservationLogsUseCase(
    private val repository: WeatherObservationRepository,
) {
    public fun observe(): Flow<List<WeatherObservationRecordDD>> {
        return repository.observeAll()
    }
}
