package io.pylyp.weather.domain.repository

import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import kotlinx.coroutines.flow.Flow

public interface WeatherObservationRepository {
    public fun observeAll(): Flow<List<WeatherObservationRecordDD>>

    public suspend fun getById(id: Long): WeatherObservationRecordDD?

    public suspend fun insert(record: WeatherObservationRecordDD): Long

    public suspend fun deleteById(id: Long)
}
