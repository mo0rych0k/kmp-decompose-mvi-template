package io.pylyp.weather.domain.entity

import kotlinx.serialization.Serializable

@Serializable
public enum class WeatherServiceType {
    OPEN_METEO,
    WTTR_IN,
    MET_NORWAY,
}

public fun WeatherServiceType.displayName(): String =
    when (this) {
        WeatherServiceType.OPEN_METEO -> "Open-Meteo"
        WeatherServiceType.WTTR_IN -> "wttr.in"
        WeatherServiceType.MET_NORWAY -> "MET Norway"
    }
