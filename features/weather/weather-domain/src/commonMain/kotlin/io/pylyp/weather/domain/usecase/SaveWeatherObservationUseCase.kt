package io.pylyp.weather.domain.usecase

import io.pylyp.common.core.foundation.entity.SuspendUseCase
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD
import io.pylyp.weather.domain.entity.isHighTemperatureDiscrepancy
import io.pylyp.weather.domain.entity.roundedForStorage
import io.pylyp.weather.domain.entity.temperatureDiscrepancyLevel
import io.pylyp.weather.domain.repository.WeatherObservationRepository
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

public data class SaveWeatherObservationParams(
    public val coordinates: GeoCoordinatesDD,
    public val locationLabel: String?,
    public val userTemperatureC: Double,
    public val userWindDirection: WindDirectionDD,
    public val userWindStrengthPercent: Int,
    public val userWeatherType: WeatherTypeDD,
    public val apiWeather: CommonWeatherDD,
)

@OptIn(ExperimentalTime::class)
public class SaveWeatherObservationUseCase(
    private val repository: WeatherObservationRepository,
) : SuspendUseCase<SaveWeatherObservationParams, Long>() {
    override suspend fun execute(parameters: SaveWeatherObservationParams): Long {
        val userT = parameters.userTemperatureC
        val apiT = parameters.apiWeather.temperatureC
        val delta = if (apiT != null) {
            userT - apiT
        } else {
            0.0
        }
        val level = temperatureDiscrepancyLevel(delta)
        val high = isHighTemperatureDiscrepancy(delta)
        val now = Clock.System.now().toEpochMilliseconds()
        val coords = parameters.coordinates.roundedForStorage()
        val record = WeatherObservationRecordDD(
            id = 0L,
            createdAtEpochMs = now,
            latitude = coords.latitude,
            longitude = coords.longitude,
            locationLabel = parameters.locationLabel?.takeIf { it.isNotBlank() },
            userTemperatureC = parameters.userTemperatureC,
            userHumidityPercent = parameters.apiWeather.humidityPercent?.coerceIn(0, 100) ?: 50,
            userPressureMmHg = parameters.apiWeather.pressureMmHg?.roundToInt()?.coerceIn(680, 820) ?: 760,
            userWindDirection = parameters.userWindDirection,
            userWindStrengthPercent = parameters.userWindStrengthPercent.coerceIn(0, 100),
            userWeatherType = parameters.userWeatherType,
            apiTemperatureC = parameters.apiWeather.temperatureC,
            apiHumidityPercent = parameters.apiWeather.humidityPercent,
            apiPressureMmHg = parameters.apiWeather.pressureMmHg,
            apiWindDescription = parameters.apiWeather.windDescription,
            apiDescription = parameters.apiWeather.description,
            temperatureDeltaC = delta,
            discrepancyLevel = level,
            isHighDiscrepancy = high,
        )
        return repository.insert(record)
    }
}
