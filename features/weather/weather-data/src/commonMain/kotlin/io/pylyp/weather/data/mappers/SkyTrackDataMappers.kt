package io.pylyp.weather.data.mappers

import io.pylyp.common.core.persistence.entity.WeatherObservationLogSD
import io.pylyp.weather.data.network.entity.OpenMeteoCurrentForecastResponseND
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.TemperatureDiscrepancyLevel
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD

private const val HPA_TO_MMHG = 0.750061683

internal fun OpenMeteoCurrentForecastResponseND.toCommonWeather(): CommonWeatherDD {
    val c = current
    val pressureHpa = c?.surfacePressureHpa
    val pressureMm = pressureHpa?.times(HPA_TO_MMHG)
    val windDeg = c?.windDirection10m
    return CommonWeatherDD(
        temperatureC = c?.temperature2m,
        humidityPercent = c?.relativeHumidity2m,
        pressureMmHg = pressureMm,
        windDirectionDeg = windDeg,
        windDescription = windDeg?.let { windDegreeToCompass(it) },
        description = c?.weatherCode?.let { openMeteoCodeToText(it) },
    )
}

private fun windDegreeToCompass(deg: Int): String {
    val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((deg.toDouble() + 22.5) / 45.0).toInt() % 8
    return directions[index]
}

@Suppress("CyclomaticComplexMethod")
private fun openMeteoCodeToText(code: Int): String =
    when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Mainly clear / partly cloudy / overcast"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing drizzle"
        61, 63, 65 -> "Rain"
        66, 67 -> "Freezing rain"
        71, 73, 75 -> "Snow fall"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> "Weather code $code"
    }

internal fun WeatherObservationLogSD.toDomain(): WeatherObservationRecordDD {
    return WeatherObservationRecordDD(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        latitude = latitude,
        longitude = longitude,
        locationLabel = locationLabel,
        userTemperatureC = userTemperatureC,
        userHumidityPercent = userHumidityPercent,
        userPressureMmHg = userPressureMmHg,
        userWindDirection = WindDirectionDD.valueOf(userWindDirectionKey),
        userWindStrengthPercent = userWindStrengthPercent,
        userWeatherType = WeatherTypeDD.valueOf(userWeatherTypeKey),
        apiTemperatureC = apiTemperatureC,
        apiHumidityPercent = apiHumidityPercent,
        apiPressureMmHg = apiPressureMmHg,
        apiWindDescription = apiWindDescription,
        apiDescription = apiDescription,
        temperatureDeltaC = temperatureDeltaC,
        discrepancyLevel = TemperatureDiscrepancyLevel.valueOf(discrepancyLevelKey),
        isHighDiscrepancy = isHighDiscrepancy,
    )
}

internal fun WeatherObservationRecordDD.toStorage(): WeatherObservationLogSD {
    return WeatherObservationLogSD(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        latitude = latitude,
        longitude = longitude,
        locationLabel = locationLabel,
        userTemperatureC = userTemperatureC,
        userHumidityPercent = userHumidityPercent,
        userPressureMmHg = userPressureMmHg,
        userWindDirectionKey = userWindDirection.name,
        userWindStrengthPercent = userWindStrengthPercent,
        userWeatherTypeKey = userWeatherType.name,
        apiTemperatureC = apiTemperatureC,
        apiHumidityPercent = apiHumidityPercent,
        apiPressureMmHg = apiPressureMmHg,
        apiWindDescription = apiWindDescription,
        apiDescription = apiDescription,
        temperatureDeltaC = temperatureDeltaC,
        discrepancyLevelKey = discrepancyLevel.name,
        isHighDiscrepancy = isHighDiscrepancy,
    )
}
