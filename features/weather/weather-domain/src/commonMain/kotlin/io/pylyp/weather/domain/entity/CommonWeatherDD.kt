package io.pylyp.weather.domain.entity

/**
 * Normalized current weather (e.g. from [Open-Meteo](https://open-meteo.com/)) for SkyTrack comparison.
 */
public data class CommonWeatherDD(
    public val temperatureC: Double?,
    public val humidityPercent: Int?,
    public val pressureMmHg: Double?,
    public val windDirectionDeg: Int?,
    public val windDescription: String?,
    public val description: String?,
) {
    public companion object {
        /** Used when the reference API call fails; user observation can still be saved. */
        public val Empty: CommonWeatherDD = CommonWeatherDD(
            temperatureC = null,
            humidityPercent = null,
            pressureMmHg = null,
            windDirectionDeg = null,
            windDescription = null,
            description = null,
        )
    }
}
