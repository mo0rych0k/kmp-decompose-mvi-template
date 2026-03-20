package io.pylyp.weather.domain.entity

public data class WeatherObservationRecordDD(
    public val id: Long = 0L,
    public val createdAtEpochMs: Long,
    public val latitude: Double?,
    public val longitude: Double?,
    /** Locality / area from reverse geocoding (mobile); optional. */
    public val locationLabel: String?,
    public val userTemperatureC: Double,
    public val userHumidityPercent: Int,
    public val userPressureMmHg: Int,
    public val userWindDirection: WindDirectionDD,
    /** User-estimated wind strength 0–100 (abstract scale). */
    public val userWindStrengthPercent: Int,
    public val userWeatherType: WeatherTypeDD,
    public val apiTemperatureC: Double?,
    public val apiHumidityPercent: Int?,
    public val apiPressureMmHg: Double?,
    public val apiWindDescription: String?,
    public val apiDescription: String?,
    public val temperatureDeltaC: Double,
    public val discrepancyLevel: TemperatureDiscrepancyLevel,
    public val isHighDiscrepancy: Boolean,
)
