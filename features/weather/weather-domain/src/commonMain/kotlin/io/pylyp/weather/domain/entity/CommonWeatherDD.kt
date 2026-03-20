package io.pylyp.weather.domain.entity

/**
 * Normalized current weather from OpenWeather (or compatible) for SkyTrack comparison.
 */
public data class CommonWeatherDD(
    public val temperatureC: Double?,
    public val humidityPercent: Int?,
    public val pressureMmHg: Double?,
    public val windDirectionDeg: Int?,
    public val windDescription: String?,
    public val description: String?,
)
