package io.pylyp.weather.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OpenWeatherCurrentResponseND(
    val main: OpenWeatherMainND? = null,
    val wind: OpenWeatherWindND? = null,
    val weather: List<OpenWeatherWeatherItemND>? = null,
)

@Serializable
public data class OpenWeatherMainND(
    val temp: Double? = null,
    val humidity: Int? = null,
    @SerialName("pressure")
    val pressureHpa: Double? = null,
)

@Serializable
public data class OpenWeatherWindND(
    val deg: Int? = null,
    val speed: Double? = null,
)

@Serializable
public data class OpenWeatherWeatherItemND(
    val description: String? = null,
)
