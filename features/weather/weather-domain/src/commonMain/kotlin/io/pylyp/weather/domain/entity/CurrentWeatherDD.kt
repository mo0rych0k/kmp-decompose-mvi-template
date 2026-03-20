package io.pylyp.weather.domain.entity

public data class CurrentWeatherDD(
    val serviceType: WeatherServiceType,
    val locationName: String,
    val temperatureC: Double?,
    val conditionDescription: String?,
    val windSpeedKmh: Double?,
    val humidityPercent: Int?,
    val observedAt: String?,
)
