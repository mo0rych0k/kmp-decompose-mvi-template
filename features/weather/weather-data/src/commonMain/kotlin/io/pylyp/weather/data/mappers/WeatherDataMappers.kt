package io.pylyp.weather.data.mappers

import io.pylyp.weather.data.network.entity.MetNoForecastCompactND
import io.pylyp.weather.data.network.entity.OpenMeteoForecastResponseND
import io.pylyp.weather.data.network.entity.WttrInResponseND
import io.pylyp.weather.domain.entity.CurrentWeatherDD
import io.pylyp.weather.domain.entity.WeatherServiceType

private const val KYIV = "Kyiv"

internal fun OpenMeteoForecastResponseND.toDomain(): CurrentWeatherDD {
    val cw = currentWeather
    return CurrentWeatherDD(
        serviceType = WeatherServiceType.OPEN_METEO,
        locationName = KYIV,
        temperatureC = cw?.temperature,
        conditionDescription = cw?.weatherCode?.let { openMeteoCodeToText(it) },
        windSpeedKmh = cw?.windspeed,
        humidityPercent = null,
        observedAt = cw?.time,
    )
}

internal fun WttrInResponseND.toDomain(): CurrentWeatherDD {
    val cc = data.currentCondition.firstOrNull()
    return CurrentWeatherDD(
        serviceType = WeatherServiceType.WTTR_IN,
        locationName = KYIV,
        temperatureC = cc?.tempC?.toDoubleOrNull(),
        conditionDescription = cc?.weatherDesc?.firstOrNull()?.value,
        windSpeedKmh = cc?.windspeedKmph?.toDoubleOrNull(),
        humidityPercent = cc?.humidity?.toIntOrNull(),
        observedAt = cc?.observationTime,
    )
}

internal fun MetNoForecastCompactND.toDomain(): CurrentWeatherDD {
    val first = properties.timeseries.firstOrNull()
    val details = first?.data?.instant?.details
    val symbol = first?.data?.next1Hours?.summary?.symbolCode
    val windMs = details?.windSpeed
    val windKmh = windMs?.let { it * 3.6 }
    return CurrentWeatherDD(
        serviceType = WeatherServiceType.MET_NORWAY,
        locationName = KYIV,
        temperatureC = details?.airTemperature,
        conditionDescription = symbol?.replace('_', ' '),
        windSpeedKmh = windKmh,
        humidityPercent = null,
        observedAt = first?.time,
    )
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
