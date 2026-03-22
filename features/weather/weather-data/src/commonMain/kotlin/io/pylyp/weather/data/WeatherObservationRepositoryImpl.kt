package io.pylyp.weather.data

import io.pylyp.common.core.persistence.WeatherObservationStorage
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.mappers.toDomain
import io.pylyp.weather.data.mappers.toStorage
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.repository.WeatherObservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class WeatherObservationRepositoryImpl(
    private val storage: WeatherObservationStorage,
    private val dispatcherProvider: DispatcherProvider,
) : WeatherObservationRepository {

    override fun observeAll(): Flow<List<WeatherObservationRecordDD>> {
        return storage.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(dispatcherProvider.IO)
    }

    override suspend fun getById(id: Long): WeatherObservationRecordDD? {
        return withContext(dispatcherProvider.IO) {
            storage.getById(id)?.toDomain()
        }
    }

    override suspend fun insert(record: WeatherObservationRecordDD): Long {
        return withContext(dispatcherProvider.IO) {
            storage.insert(record.toStorage())
        }
    }

    override suspend fun deleteById(id: Long) {
        withContext(dispatcherProvider.IO) {
            storage.deleteById(id)
        }
    }
}
