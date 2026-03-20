package io.pylyp.weather.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WttrInResponseND(
    val data: WttrInDataND,
)

@Serializable
public data class WttrInDataND(
    @SerialName("current_condition") val currentCondition: List<WttrCurrentConditionND> = emptyList(),
)

@Serializable
public data class WttrCurrentConditionND(
    @SerialName("temp_C") val tempC: String? = null,
    @SerialName("windspeedKmph") val windspeedKmph: String? = null,
    val humidity: String? = null,
    @SerialName("observation_time") val observationTime: String? = null,
    @SerialName("weatherDesc") val weatherDesc: List<WttrWeatherDescND> = emptyList(),
)

@Serializable
public data class WttrWeatherDescND(
    val value: String? = null,
)
