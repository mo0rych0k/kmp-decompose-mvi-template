package io.pylyp.weather.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OpenMeteoForecastResponseND(
    @SerialName("current_weather") val currentWeather: OpenMeteoCurrentWeatherND? = null,
)

@Serializable
public data class OpenMeteoCurrentWeatherND(
    val time: String? = null,
    val temperature: Double? = null,
    val windspeed: Double? = null,
    val winddirection: Int? = null,
    @SerialName("weathercode") val weatherCode: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
)
