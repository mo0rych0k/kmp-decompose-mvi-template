package io.pylyp.weather.data.mappers

import io.pylyp.common.core.persistence.entity.WeatherObservationLogSD
import io.pylyp.weather.data.network.entity.OpenWeatherCurrentResponseND
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.TemperatureDiscrepancyLevel
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD

private const val HPA_TO_MMHG = 0.750061683

internal fun OpenWeatherCurrentResponseND.toCommonWeather(): CommonWeatherDD {
    val m = main
    val w = wind
    val pressureHpa = m?.pressureHpa
    val pressureMm = pressureHpa?.times(HPA_TO_MMHG)
    val windDeg = w?.deg
    return CommonWeatherDD(
        temperatureC = m?.temp,
        humidityPercent = m?.humidity,
        pressureMmHg = pressureMm,
        windDirectionDeg = windDeg,
        windDescription = windDeg?.let { windDegreeToCompass(it) },
        description = weather?.firstOrNull()?.description,
    )
}

private fun windDegreeToCompass(deg: Int): String {
    val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((deg.toDouble() + 22.5) / 45.0).toInt() % 8
    return directions[index]
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
