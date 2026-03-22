package io.pylyp.common.core.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.pylyp.common.core.persistence.entity.WeatherObservationLogSD
import kotlinx.coroutines.flow.Flow

@Dao
internal interface WeatherLogDao {
    @Query("SELECT * FROM weather_logs ORDER BY createdAtEpochMs DESC")
    public fun observeAll(): Flow<List<WeatherObservationLogSD>>

    @Query("SELECT * FROM weather_logs WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): WeatherObservationLogSD?

    @Insert
    public suspend fun insert(entity: WeatherObservationLogSD): Long

    @Query("DELETE FROM weather_logs WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
