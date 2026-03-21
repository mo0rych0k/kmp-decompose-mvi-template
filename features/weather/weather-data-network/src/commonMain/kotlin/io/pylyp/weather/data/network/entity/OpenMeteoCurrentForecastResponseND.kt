package io.pylyp.weather.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Forecast API response with a `current` block (see [Open-Meteo docs](https://open-meteo.com/)). */
@Serializable
public data class OpenMeteoCurrentForecastResponseND(
    val current: OpenMeteoCurrentND? = null,
)

@Serializable
public data class OpenMeteoCurrentND(
    val time: String? = null,
    @SerialName("temperature_2m") val temperature2m: Double? = null,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: Int? = null,
    @SerialName("surface_pressure") val surfacePressureHpa: Double? = null,
    @SerialName("wind_speed_10m") val windSpeed10m: Double? = null,
    @SerialName("wind_direction_10m") val windDirection10m: Int? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
)
